package com.example.ecomm_orderservice.services.cart;

import com.example.ecomm_orderservice.dto.AddProductInCartDto;
import com.example.ecomm_orderservice.dto.OrderDto;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);

    OrderDto getCartByUserId (Long userId);

    OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto);

    OrderDto decreaseProductQuantity (AddProductInCartDto addProductInCartDto);
}
