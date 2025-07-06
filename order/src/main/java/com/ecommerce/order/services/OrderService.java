package com.ecommerce.order.services;


import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.models.Order;
import com.ecommerce.order.models.OrderItem;
import com.ecommerce.order.models.OrderStatus;
import com.ecommerce.order.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
//    private final UserRepository userRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        //validate cartitem
        List<CartItem> cartItems = cartService.getCart(userId);
        if(cartItems == null || cartItems.isEmpty()){
            return Optional.empty();
        }
        //validate for user
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if(userOptional.isEmpty()){
//            return Optional.empty();
//        }
//        User user = userOptional.get();
//        //calculate total price
        BigDecimal totalPrice  = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //create order
        Order order = new Order();
//        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
//        List<OrderItem> orderItems = cartItems.stream()
//                .map(item -> new OrderItem(
//                        null,
////                        item.getProduct(),
//                        item.getQuantity(),
//                        item.getPrice(),
//                        order
//
//                )).toList();

//        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        //clear the cart

        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));


    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
        return new OrderResponse();
//        return new OrderResponse(
//                savedOrder.getId(),
//                savedOrder.getTotalAmount(),
//                savedOrder.getStatus(),
//                savedOrder.getItems().stream()
//                        .map(orderItem -> new OrderItemDTO(
//                                orderItem.getId(),
//                                orderItem.getProduct().getId(),
//                                orderItem.getQuantity(),
//                                orderItem.getPrice(),
//                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
//                        )).toList(),
//                savedOrder.getCreatedAt()
//        );
    }
}
