package com.example.ecomm_orderservice.services.admin.adminOrder;

import com.example.ecomm_orderservice.dto.OrderDto;
import com.example.ecomm_orderservice.entity.Order;
import com.example.ecomm_orderservice.enums.OrderStatus;
import com.example.ecomm_orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    public List<OrderDto> getAllPlacedOrders() {
        List<Order> orderList = orderRepository.
                findAllByOrderStatusIn (List.of (OrderStatus. Placed, OrderStatus. Shipped, OrderStatus. Delivered));

        return orderList.stream().map(Order::getOrderDto).collect (Collectors.toList());
    }

    public OrderDto changeOrderStatus (Long orderId, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isPresent() ) {
            Order order = optionalOrder.get();
            if(Objects.equals(status, "Shipped")){
                order.setOrderStatus (OrderStatus. Shipped);
            }else if(Objects.equals(status,"Delivered")){
                order.setOrderStatus (OrderStatus. Delivered);
            }
            return orderRepository.save(order).getOrderDto();
        }
        return null;
    }
}
