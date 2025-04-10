package com.example.ecomm_productservice.services.admin.adminproduct;

import com.example.ecomm_productservice.dto.ProductDto;
import com.example.ecomm_productservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface AdminProductService {

    ProductDto addProduct (ProductDto productDto) throws IOException;

    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsByName(String name);

    boolean deleteProduct (Long id);

//    Page<Product> getPaginatedProducts(Pageable pageable);

}
