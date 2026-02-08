package com.kanga.kickrushapi.order;

import com.kanga.kickrush.domain.order.Order;
import com.kanga.kickrush.domain.release.Release;
import com.kanga.kickrush.domain.release.ReleaseService;
import com.kanga.kickrush.domain.releaseSize.ReleaseSize;
import com.kanga.kickrush.domain.releaseSize.ReleaseSizeService;
import com.kanga.kickrush.domain.shoe.Shoe;
import com.kanga.kickrush.domain.shoe.ShoeService;
import com.kanga.kickrushapi.common.ApiException;
import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.order.dto.CancelOrderResponse;
import com.kanga.kickrushapi.order.dto.CreateOrderResponse;
import com.kanga.kickrushapi.order.dto.OrderDetailDto;
import com.kanga.kickrushapi.order.dto.OrderDto;
import com.kanga.kickrushapi.order.dto.ShoeMiniDto;
import com.kanga.kickrushapi.order.dto.ShoeSummaryWithImageAndModelDto;
import com.kanga.kickrushapi.order.dto.ShoeSummaryWithImageDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service("orderApiService")
public class OrderService {

    private final com.kanga.kickrush.domain.order.OrderService coreOrderService;
    private final ReleaseSizeService releaseSizeService;
    private final ReleaseService releaseService;
    private final ShoeService shoeService;

    public OrderService(com.kanga.kickrush.domain.order.OrderService coreOrderService,
            ReleaseSizeService releaseSizeService,
            ReleaseService releaseService,
            ShoeService shoeService) {
        this.coreOrderService = coreOrderService;
        this.releaseSizeService = releaseSizeService;
        this.releaseService = releaseService;
        this.shoeService = shoeService;
    }

    public CreateOrderResponse createOrder(Long memberId, Long releaseSizeId) {
        if (releaseSizeId == null) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER, HttpStatus.BAD_REQUEST, "releaseSizeId가 필요합니다.");
        }
        Order order = handleCoreExceptions(() -> coreOrderService.createOrder(memberId, releaseSizeId));
        ReleaseSize releaseSize = findReleaseSize(releaseSizeId);
        Shoe shoe = findShoe(findRelease(releaseSize.getReleaseId()).getShoeId());
        return new CreateOrderResponse(order.getId(), order.getReleaseSizeId(), releaseSize.getSize(),
                new ShoeMiniDto(shoe.getId(), shoe.getName(), shoe.getBrand()),
                releaseSize.getPrice(), order.getStatus().name(), order.getOrderedAt().toString());
    }

    public List<OrderDto> listMyOrders(Long memberId) {
        return coreOrderService.findByMemberId(memberId).stream()
                .map(this::toOrderDto)
                .collect(Collectors.toList());
    }

    public OrderDetailDto getMyOrder(Long memberId, Long orderId) {
        Order order = handleCoreExceptions(() -> coreOrderService.getByMemberAndId(memberId, orderId));
        ReleaseSize releaseSize = findReleaseSize(order.getReleaseSizeId());
        Release release = findRelease(releaseSize.getReleaseId());
        Shoe shoe = findShoe(release.getShoeId());
        return new OrderDetailDto(order.getId(),
                new ShoeSummaryWithImageAndModelDto(shoe.getId(), shoe.getName(), shoe.getBrand(),
                        shoe.getModelNumber(), shoe.getImageUrl()),
                releaseSize.getSize(), releaseSize.getPrice(), order.getStatus().name(),
                order.getOrderedAt().toString(),
                order.getCancelledAt() == null ? null : order.getCancelledAt().toString());
    }

    public CancelOrderResponse cancelOrder(Long memberId, Long orderId) {
        Order order = handleCoreExceptions(() -> coreOrderService.cancelOrder(memberId, orderId));
        return new CancelOrderResponse(order.getId(), order.getStatus().name(),
                order.getCancelledAt() == null ? null : order.getCancelledAt().toString());
    }

    private OrderDto toOrderDto(Order order) {
        ReleaseSize releaseSize = findReleaseSize(order.getReleaseSizeId());
        Release release = findRelease(releaseSize.getReleaseId());
        Shoe shoe = findShoe(release.getShoeId());
        return new OrderDto(order.getId(), new ShoeSummaryWithImageDto(shoe.getId(), shoe.getName(),
                shoe.getBrand(), shoe.getImageUrl()), releaseSize.getSize(),
                releaseSize.getPrice(), order.getStatus().name(), order.getOrderedAt().toString());
    }

    private ReleaseSize findReleaseSize(Long id) {
        try {
            return releaseSizeService.findById(id);
        } catch (IllegalArgumentException ex) {
            throw new ApiException(ErrorCode.RELEASE_NOT_FOUND, HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private Release findRelease(Long id) {
        try {
            return releaseService.findById(id);
        } catch (IllegalArgumentException ex) {
            throw new ApiException(ErrorCode.RELEASE_NOT_FOUND, HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private Shoe findShoe(Long id) {
        try {
            return shoeService.findById(id);
        } catch (IllegalArgumentException ex) {
            throw new ApiException(ErrorCode.SHOE_NOT_FOUND, HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private <T> T handleCoreExceptions(java.util.concurrent.Callable<T> action) {
        try {
            return action.call();
        } catch (IllegalStateException ex) {
            ErrorCode code = mapCoreErrorCode(ex.getMessage());
            throw new ApiException(code, mapStatus(code), ex.getMessage());
        } catch (IllegalArgumentException ex) {
            ErrorCode code = mapCoreArgumentErrorCode(ex.getMessage());
            throw new ApiException(code, mapStatus(code), ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private ErrorCode mapCoreErrorCode(String code) {
        if ("DUPLICATE_ORDER".equals(code)) {
            return ErrorCode.DUPLICATE_ORDER;
        }
        if ("RELEASE_NOT_STARTED".equals(code)) {
            return ErrorCode.RELEASE_NOT_STARTED;
        }
        if ("RELEASE_ENDED".equals(code)) {
            return ErrorCode.RELEASE_ENDED;
        }
        if ("LOCK_TIMEOUT".equals(code)) {
            return ErrorCode.LOCK_TIMEOUT;
        }
        if ("ORDER_NOT_CANCELLABLE".equals(code)) {
            return ErrorCode.ORDER_NOT_CANCELLABLE;
        }
        return ErrorCode.INVALID_PARAMETER;
    }

    private ErrorCode mapCoreArgumentErrorCode(String message) {
        if ("재고가 부족합니다".equals(message)) {
            return ErrorCode.STOCK_INSUFFICIENT;
        }
        if ("사이즈별 재고를 찾을 수 없습니다".equals(message)) {
            return ErrorCode.RELEASE_NOT_FOUND;
        }
        if ("발매를 찾을 수 없습니다".equals(message)) {
            return ErrorCode.RELEASE_NOT_FOUND;
        }
        if ("주문 정보를 찾을 수 없습니다".equals(message)) {
            return ErrorCode.ORDER_NOT_FOUND;
        }
        return ErrorCode.INVALID_PARAMETER;
    }

    private HttpStatus mapStatus(ErrorCode code) {
        return switch (code) {
            case DUPLICATE_ORDER, ORDER_NOT_CANCELLABLE, STOCK_INSUFFICIENT -> HttpStatus.CONFLICT;
            case RELEASE_NOT_STARTED, RELEASE_ENDED, INVALID_PARAMETER -> HttpStatus.BAD_REQUEST;
            case RELEASE_NOT_FOUND, ORDER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case LOCK_TIMEOUT -> HttpStatus.SERVICE_UNAVAILABLE;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
