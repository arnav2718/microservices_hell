package com.example.ecomm_productservice.services.customer;

import com.example.ecomm_productservice.dto.ProductDetailDto;
import com.example.ecomm_productservice.dto.ProductDto;
import com.example.ecomm_productservice.entity.FAQ;
import com.example.ecomm_productservice.entity.Product;
import com.example.ecomm_productservice.entity.Review;
import com.example.ecomm_productservice.repository.FAQRepository;
import com.example.ecomm_productservice.repository.ProductRepository;
import com.example.ecomm_productservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService {

    private final ProductRepository productRepository;

    private final FAQRepository faqRepository;

    private final ReviewRepository reviewRepository;

    public List<ProductDto> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public List<ProductDto> searchProductByTitle(String name){
        List<Product> products = productRepository.findAllByNameContaining(name);
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public ProductDetailDto getProductDetailById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            List<FAQ> faqList = faqRepository.findAllByProductId (productId);
            List<Review> reviewsList = reviewRepository.findAllByProductId (productId);

            ProductDetailDto productDetailDto = new ProductDetailDto();

            productDetailDto.setProductDto (optionalProduct.get().getDto());
            productDetailDto. setFaqDtoList (faqList.stream().map(FAQ::getFAQDto).collect (Collectors.toList()));
            productDetailDto.setReviewDtoList(reviewsList.stream().map(Review::getDto).collect (Collectors.toList()));

            return productDetailDto;

        }
        return null;
    }
}
