package com.example.ecomm_orderservice.services.wishlist;

import com.example.ecomm_orderservice.dto.WishlistDto;
import com.example.ecomm_orderservice.entity.Product;
import com.example.ecomm_orderservice.entity.User;
import com.example.ecomm_orderservice.entity.Wishlist;
import com.example.ecomm_orderservice.repository.ProductRepository;
import com.example.ecomm_orderservice.repository.UserRepository;
import com.example.ecomm_orderservice.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final WishlistRepository wishlistRepository;

    public WishlistDto addProductToWishlist (WishlistDto wishlistDto){
        Optional<Product> optionalProduct = productRepository.findById(wishlistDto.getProductId());
        Optional <User> optionalUser = userRepository.findById(wishlistDto.getUserId());
        if (optionalProduct.isPresent ( ) && optionalUser.isPresent()) {
            Wishlist wishlist = new Wishlist();
            wishlist.setProduct (optionalProduct.get());
            wishlist.setUser(optionalUser.get());
            return wishlistRepository.save(wishlist).getWishlistDto();
        }
        return null;
    }

    public List<WishlistDto> getWishlistByUserId(Long userId){
        return wishlistRepository.findAllByUserId(userId).stream().map(Wishlist::getWishlistDto).collect(Collectors.toList());
    }

}
