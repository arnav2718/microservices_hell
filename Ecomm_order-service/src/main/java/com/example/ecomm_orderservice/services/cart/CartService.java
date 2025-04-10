package com.example.ecomm_orderservice.services.cart;

import com.example.ecomm_orderservice.dto.AddProductInCartDto;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);
}
