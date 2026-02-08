package com.kanga.kickrush.domain.releaseSize;

import com.kanga.kickrush.TestConfiguration;
import com.kanga.kickrush.domain.release.Release;
import com.kanga.kickrush.domain.release.ReleaseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestConfiguration.class)
class ReleaseSizeConcurrencyTest {

    @Autowired
    private ReleaseSizeRepository releaseSizeRepository;

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private ReleaseSizeService releaseSizeService;

    @Test
    @DisplayName("동시 재고 차감 시 초과 판매를 방지한다 (Pessimistic Lock)")
    void shouldPreventOversellWithPessimisticLock() throws Exception {
        Release release = createRelease();
        ReleaseSize saved = releaseSizeRepository.save(ReleaseSize.builder()
                .releaseId(release.getId())
                .size(270)
                .stock(10)
                .price(300000)
                .build());

        int threads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicInteger successCount = new AtomicInteger();

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    start.await();
                    releaseSizeService.decreaseStockWithLock(saved.getId(), 1);
                    successCount.incrementAndGet();
                } catch (Exception ignored) {
                    // 재고 부족 또는 기타 예외는 실패로 처리
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        done.await(5, TimeUnit.SECONDS);
        executor.shutdownNow();

        ReleaseSize updated = releaseSizeRepository.findById(saved.getId()).orElseThrow();
        assertThat(successCount.get()).isEqualTo(10);
        assertThat(updated.getStock()).isEqualTo(0);
    }

    private Release createRelease() {
        LocalDateTime now = LocalDateTime.now();
        return releaseRepository.save(Release.builder()
                .shoeId(1L)
                .releaseDateTime(now.minusMinutes(1))
                .endDateTime(now.plusMinutes(10))
                .totalStock(100)
                .build());
    }
}
