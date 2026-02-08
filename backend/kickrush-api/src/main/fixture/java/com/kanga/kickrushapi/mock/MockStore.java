package com.kanga.kickrushapi.mock;

import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.store.Store;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MockStore implements Store {

    private final AtomicLong orderId = new AtomicLong(1002L);
    private final AtomicLong releaseId = new AtomicLong(3L);
    private final AtomicLong shoeId = new AtomicLong(5L);
    private final AtomicLong memberId = new AtomicLong(2L);
    private final AtomicLong releaseSizeId = new AtomicLong(21L);

    private final List<Shoe> shoes = new ArrayList<>();
    private final List<Release> releases = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    public MockStore() {
        shoes.add(new Shoe(1L, "Air Jordan 1 Retro High OG 'Chicago Lost and Found'", "Nike", "DZ5485-612", 209000,
                "1985년 오리지널 시카고 컬러웨이를 재현한 에어 조던 1.", "/images/shoes/aj1-chicago.jpg"));
        shoes.add(new Shoe(2L, "Nike Dunk Low 'Panda'", "Nike", "DD1391-100", 139000,
                "클래식한 블랙 앤 화이트 컬러웨이의 덩크 로우.", "/images/shoes/dunk-panda.jpg"));
        shoes.add(new Shoe(3L, "New Balance 550 'White Green'", "New Balance", "BB550WT1", 139000,
                "90년대 농구화에서 영감받은 레트로 디자인.", "/images/shoes/nb550-green.jpg"));
        shoes.add(new Shoe(4L, "Adidas Samba OG 'White Black'", "Adidas", "B75806", 129000,
                "1950년대 인도어 축구화에서 시작된 아디다스 삼바.", "/images/shoes/samba-og.jpg"));
        shoes.add(new Shoe(5L, "Nike Air Force 1 '07 Low 'White'", "Nike", "CW2288-111", 139000,
                "1982년 처음 출시된 에어포스 1의 클래식 화이트 버전.", "/images/shoes/af1-white.jpg"));

        releases.add(Release.sample(1L, 1L, LocalDateTime.of(2026, 2, 10, 11, 0),
                LocalDateTime.of(2026, 2, 10, 11, 30), ReleaseStatus.UPCOMING, 100,
                List.of(
                        new ReleaseSize(1L, 250, 10, 209000),
                        new ReleaseSize(2L, 255, 8, 209000),
                        new ReleaseSize(3L, 260, 12, 209000),
                        new ReleaseSize(4L, 265, 15, 209000),
                        new ReleaseSize(5L, 270, 20, 209000),
                        new ReleaseSize(6L, 275, 15, 209000),
                        new ReleaseSize(7L, 280, 10, 209000),
                        new ReleaseSize(8L, 285, 5, 209000),
                        new ReleaseSize(9L, 290, 5, 209000)
                )));
        releases.add(Release.sample(2L, 2L, LocalDateTime.of(2026, 2, 8, 10, 0),
                LocalDateTime.of(2026, 2, 8, 10, 30), ReleaseStatus.ONGOING, 50,
                List.of(
                        new ReleaseSize(10L, 250, 5, 139000),
                        new ReleaseSize(11L, 255, 3, 139000),
                        new ReleaseSize(12L, 260, 8, 139000),
                        new ReleaseSize(13L, 265, 10, 139000),
                        new ReleaseSize(14L, 270, 12, 139000),
                        new ReleaseSize(15L, 275, 7, 139000),
                        new ReleaseSize(16L, 280, 5, 139000)
                )));
        releases.add(Release.sample(3L, 3L, LocalDateTime.of(2026, 2, 5, 11, 0),
                LocalDateTime.of(2026, 2, 5, 11, 30), ReleaseStatus.ENDED, 0,
                List.of(
                        new ReleaseSize(17L, 250, 0, 139000),
                        new ReleaseSize(18L, 255, 0, 139000),
                        new ReleaseSize(19L, 260, 0, 139000),
                        new ReleaseSize(20L, 265, 0, 139000),
                        new ReleaseSize(21L, 270, 0, 139000)
                )));

        members.add(new Member(1L, "user@example.com", "SecureP@ss123", "홍길동", MemberRole.USER));
        members.add(new Member(2L, "admin@example.com", "AdminP@ss123", "관리자", MemberRole.ADMIN));

        orders.add(new Order(1001L, 1L, 5L, 1L, 270, 209000, OrderStatus.COMPLETED,
                LocalDateTime.of(2026, 2, 10, 11, 0, 5), null));
        orders.add(new Order(1002L, 1L, 14L, 2L, 270, 139000, OrderStatus.CANCELLED,
                LocalDateTime.of(2026, 2, 8, 10, 0, 10), LocalDateTime.of(2026, 2, 8, 10, 5)));
    }

    @Override
    public Optional<Shoe> findShoe(Long id) {
        return shoes.stream().filter(s -> s.id().equals(id)).findFirst();
    }

    @Override
    public Optional<Release> findRelease(Long id) {
        return releases.stream().filter(r -> r.id().equals(id)).findFirst();
    }

    @Override
    public Optional<ReleaseSize> findReleaseSize(Long id) {
        return releases.stream()
                .flatMap(r -> r.sizes().stream())
                .filter(s -> s.id().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        return members.stream().filter(m -> m.email().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public Member getMember(Long id) {
        return members.stream().filter(m -> m.id().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. id=" + id));
    }

    @Override
    public List<Shoe> shoes() {
        return shoes;
    }

    @Override
    public List<Release> releases() {
        return releases;
    }

    @Override
    public List<Member> members() {
        return members;
    }

    @Override
    public List<Order> orders() {
        return orders;
    }

    @Override
    public long nextOrderId() {
        return orderId.incrementAndGet();
    }

    @Override
    public long nextReleaseId() {
        return releaseId.incrementAndGet();
    }

    @Override
    public long nextShoeId() {
        return shoeId.incrementAndGet();
    }

    @Override
    public long nextMemberId() {
        return memberId.incrementAndGet();
    }

    @Override
    public long nextReleaseSizeId() {
        return releaseSizeId.incrementAndGet();
    }

    @Override
    public boolean isDuplicateOrder(Long memberId, Long releaseSizeId) {
        return orders.stream()
                .anyMatch(o -> o.memberId().equals(memberId) && o.releaseSizeId().equals(releaseSizeId));
    }

    @Override
    public void addOrder(Order order) {
        orders.add(order);
    }

    @Override
    public void addMember(Member member) {
        members.add(member);
    }

    @Override
    public void addShoe(Shoe shoe) {
        shoes.add(shoe);
    }

    @Override
    public void addRelease(Release release) {
        releases.add(release);
    }

    @Override
    public RuntimeException apiError(ErrorCode code, String message) {
        return new MockStoreException(code, message);
    }
}
