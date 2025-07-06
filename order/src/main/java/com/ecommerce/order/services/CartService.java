package com.ecommerce.order.services;


import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    private final CartItemRepository cartItemRepository;
    public Boolean addToCart(String userId, CartItemRequest request) {
        ProductResponse productResponse = productServiceClient.getProductDetails(String.valueOf(request.getProductId()));
        if(productResponse == null | productResponse.getStockQuantity()< request.getQuantity()){
            return false;
        }

        UserResponse userResponse = userServiceClient.getUserDetails(userId);
        if(userResponse==null){
            return false;
        }
//        User user = userOpt.get();
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId,String.valueOf(request.getProductId()));
////        System.out.println(existingCartItem);
        if(existingCartItem != null){
            existingCartItem.setQuantity(existingCartItem.getQuantity()+request.getQuantity());
            existingCartItem.setPrice(productResponse.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }else{
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(String.valueOf(productResponse.getId()));
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(productResponse.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteFromCart(String userId, Long productId) {
        ProductResponse productResponse = productServiceClient.getProductDetails(String.valueOf(productId));
//        Optional<String> userOpt = userRepository.findById(Long.valueOf(userId));
//        if(product.isPresent() && userOpt.isPresent()){
//            cartItemRepository.deleteByUserIdAndProductId(userOpt.get(),product.get());
//            return true;
//        }

        return false;
    }

    public List<CartItem> getCart(String userId) {
//        return userRepository.findById(Long.valueOf(userId))
//                .map(cartItemRepository::findByUser)
//                .orElseGet(List::of);
        return List.of();
    }

    public void clearCart(String userId) {
//        userRepository.findById(Long.valueOf(userId)).ifPresent(cartItemRepository::deleteByUser);
    }
}
