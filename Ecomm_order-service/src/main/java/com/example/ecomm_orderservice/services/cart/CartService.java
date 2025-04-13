package com.example.ecomm_orderservice.services.cart;

import com.example.ecomm_orderservice.dto.AddProductInCartDto;
import com.example.ecomm_orderservice.dto.OrderDto;
import com.example.ecomm_orderservice.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {

    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);

    OrderDto getCartByUserId (Long userId);

    OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto);

    OrderDto decreaseProductQuantity (AddProductInCartDto addProductInCartDto);

    OrderDto placeOrder (PlaceOrderDto placeOrderDto);

    List<OrderDto> getMyPlacedOrders(Long userId);

    public OrderDto searchOrderByTrackingId (UUID trackingId);
}
