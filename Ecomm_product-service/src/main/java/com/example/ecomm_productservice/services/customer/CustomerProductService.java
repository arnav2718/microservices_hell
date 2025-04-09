package com.example.ecomm_productservice.services.customer;

import com.example.ecomm_productservice.dto.ProductDto;

import java.util.List;

public interface CustomerProductService {

    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsByName(String name);

//    List<ProductDto> getAllProductsByTitle(String title);

}
