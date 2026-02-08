package com.kanga.kickrushapi.order;

import com.kanga.kickrushapi.common.PageResponse;
import com.kanga.kickrushapi.common.PageUtils;
import com.kanga.kickrushapi.security.AuthContext;
import com.kanga.kickrushapi.security.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.kanga.kickrushapi.order.dto.CancelOrderResponse;
import com.kanga.kickrushapi.order.dto.CreateOrderRequest;
import com.kanga.kickrushapi.order.dto.CreateOrderResponse;
import com.kanga.kickrushapi.order.dto.OrderDetailDto;
import com.kanga.kickrushapi.order.dto.OrderDto;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse create(@RequestBody CreateOrderRequest request) {
        AuthUser user = AuthContext.get();
        return orderService.createOrder(user.memberId(), request.releaseSizeId());
    }

    @GetMapping("/my/orders")
    public PageResponse<OrderDto> myOrders(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        AuthUser user = AuthContext.get();
        List<OrderDto> orders = orderService.listMyOrders(user.memberId());
        return PageUtils.paginate(orders, page, size);
    }

    @GetMapping("/my/orders/{id}")
    public OrderDetailDto myOrder(@PathVariable Long id) {
        AuthUser user = AuthContext.get();
        return orderService.getMyOrder(user.memberId(), id);
    }

    @PostMapping("/my/orders/{id}/cancel")
    public CancelOrderResponse cancel(@PathVariable Long id) {
        AuthUser user = AuthContext.get();
        return orderService.cancelOrder(user.memberId(), id);
    }
}
