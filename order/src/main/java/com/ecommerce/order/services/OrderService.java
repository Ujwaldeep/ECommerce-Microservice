package com.ecommerce.order.services;


import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.models.Order;
import com.ecommerce.order.models.OrderItem;
import com.ecommerce.order.models.OrderStatus;
import com.ecommerce.order.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserServiceClient userServiceClient;
    private final RabbitTemplate rabbitTemplate;
//    private final UserRepository userRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        //validate cartitem
        List<CartItem> cartItems = cartService.getCart(userId);
        if(cartItems == null || cartItems.isEmpty()){
            return Optional.empty();
        }
        //validate for user
        UserResponse user = userServiceClient.getUserDetails(userId);
        if(user == null){
            return Optional.empty();
        }
//        //calculate total price
        BigDecimal totalPrice  = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //create order
        Order order = new Order();
//        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order

                )).toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        //clear the cart

        cartService.clearCart(userId);
        //Publish order created event
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getStatus(),
                mapToOrderItemDTOs(savedOrder.getItems()),
                savedOrder.getTotalAmount(),
                savedOrder.getCreatedAt()
        );
        rabbitTemplate.convertAndSend("order.exchange", "order.tracking",
                event);

        return Optional.of(mapToOrderResponse(savedOrder));


    }

    private List<OrderItemDTO> mapToOrderItemDTOs(List<OrderItem> orderItems){
        return orderItems.stream()
                .map(item-> new OrderItemDTO(
                        item.getId(),
                        Long.valueOf(item.getProductId()),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                )).collect(Collectors.toList());
    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
//        return new OrderResponse();
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                savedOrder.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                Long.valueOf(orderItem.getProductId()),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        )).toList(),
                savedOrder.getCreatedAt()
        );
    }
}
