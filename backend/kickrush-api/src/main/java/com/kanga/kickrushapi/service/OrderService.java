package com.kanga.kickrushapi.service;

import com.kanga.kickrushapi.api.dto.CancelOrderResponse;
import com.kanga.kickrushapi.api.dto.CreateOrderResponse;
import com.kanga.kickrushapi.api.dto.OrderDetailDto;
import com.kanga.kickrushapi.api.dto.OrderDto;
import com.kanga.kickrushapi.api.dto.ShoeMiniDto;
import com.kanga.kickrushapi.api.dto.ShoeSummaryWithImageAndModelDto;
import com.kanga.kickrushapi.api.dto.ShoeSummaryWithImageDto;
import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.mock.MockStore;
import com.kanga.kickrushapi.mock.Order;
import com.kanga.kickrushapi.mock.OrderStatus;
import com.kanga.kickrushapi.mock.Release;
import com.kanga.kickrushapi.mock.ReleaseSize;
import com.kanga.kickrushapi.mock.ReleaseStatus;
import com.kanga.kickrushapi.mock.Shoe;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ReleaseQueryService releaseQueryService;

    public OrderService(ReleaseQueryService releaseQueryService) {
        this.releaseQueryService = releaseQueryService;
    }

    public CreateOrderResponse createOrder(Long memberId, Long releaseSizeId) {
        if (releaseSizeId == null) {
            throw MockStore.apiError(ErrorCode.INVALID_PARAMETER, "releaseSizeId가 필요합니다.");
        }
        Release release = releaseQueryService.findReleaseBySize(releaseSizeId);
        if (release.status() == ReleaseStatus.UPCOMING) {
            throw MockStore.apiError(ErrorCode.RELEASE_NOT_STARTED, "발매가 아직 시작되지 않았습니다.");
        }
        if (release.status() == ReleaseStatus.ENDED) {
            throw MockStore.apiError(ErrorCode.RELEASE_ENDED, "발매가 종료되었습니다.");
        }
        if (MockStore.isDuplicateOrder(memberId, releaseSizeId)) {
            throw MockStore.apiError(ErrorCode.DUPLICATE_ORDER, "이미 해당 상품을 주문하셨습니다.");
        }
        ReleaseSize releaseSize = releaseQueryService.findReleaseSize(releaseSizeId);
        if (releaseSize.stock() <= 0) {
            throw MockStore.apiError(ErrorCode.STOCK_INSUFFICIENT, "재고가 부족합니다.");
        }
        Shoe shoe = MockStore.findShoe(release.shoeId())
                .orElseThrow(() -> MockStore.apiError(ErrorCode.SHOE_NOT_FOUND, "상품을 찾을 수 없습니다."));
        long orderId = MockStore.nextOrderId();
        Order order = new Order(orderId, memberId, releaseSizeId, shoe.id(), releaseSize.size(),
                releaseSize.price(), OrderStatus.COMPLETED, LocalDateTime.now(), null);
        MockStore.ORDERS.add(order);
        return new CreateOrderResponse(order.id(), order.releaseSizeId(), order.size(),
                new ShoeMiniDto(shoe.id(), shoe.name(), shoe.brand()),
                order.price(), order.status().name(), order.orderedAt().toString());
    }

    public List<OrderDto> listMyOrders(Long memberId) {
        return MockStore.ORDERS.stream()
                .filter(order -> order.memberId().equals(memberId))
                .map(this::toOrderDto)
                .collect(Collectors.toList());
    }

    public OrderDetailDto getMyOrder(Long memberId, Long orderId) {
        Order order = MockStore.ORDERS.stream()
                .filter(o -> o.memberId().equals(memberId) && o.id().equals(orderId))
                .findFirst()
                .orElseThrow(() -> MockStore.apiError(ErrorCode.ORDER_NOT_FOUND, "주문 정보를 찾을 수 없습니다."));
        Shoe shoe = MockStore.findShoe(order.shoeId())
                .orElseThrow(() -> MockStore.apiError(ErrorCode.SHOE_NOT_FOUND, "상품을 찾을 수 없습니다."));
        return new OrderDetailDto(order.id(),
                new ShoeSummaryWithImageAndModelDto(shoe.id(), shoe.name(), shoe.brand(), shoe.modelNumber(), shoe.imageUrl()),
                order.size(), order.price(), order.status().name(), order.orderedAt().toString(),
                order.cancelledAt() == null ? null : order.cancelledAt().toString());
    }

    public CancelOrderResponse cancelOrder(Long memberId, Long orderId) {
        Order order = MockStore.ORDERS.stream()
                .filter(o -> o.memberId().equals(memberId) && o.id().equals(orderId))
                .findFirst()
                .orElseThrow(() -> MockStore.apiError(ErrorCode.ORDER_NOT_FOUND, "주문 정보를 찾을 수 없습니다."));
        if (order.status() == OrderStatus.CANCELLED) {
            throw MockStore.apiError(ErrorCode.ORDER_NOT_CANCELLABLE, "취소할 수 없는 주문입니다.");
        }
        Order cancelled = new Order(order.id(), order.memberId(), order.releaseSizeId(), order.shoeId(),
                order.size(), order.price(), OrderStatus.CANCELLED, order.orderedAt(), LocalDateTime.now());
        MockStore.ORDERS.remove(order);
        MockStore.ORDERS.add(cancelled);
        return new CancelOrderResponse(cancelled.id(), cancelled.status().name(), cancelled.cancelledAt().toString());
    }

    private OrderDto toOrderDto(Order order) {
        Shoe shoe = MockStore.findShoe(order.shoeId())
                .orElseThrow(() -> MockStore.apiError(ErrorCode.SHOE_NOT_FOUND, "상품을 찾을 수 없습니다."));
        return new OrderDto(order.id(), new ShoeSummaryWithImageDto(shoe.id(), shoe.name(), shoe.brand(),
                shoe.imageUrl()), order.size(), order.price(), order.status().name(), order.orderedAt().toString());
    }
}
