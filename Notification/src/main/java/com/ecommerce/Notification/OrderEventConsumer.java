package com.ecommerce.Notification;

import com.ecommerce.Notification.payload.OrderCreatedEvent;
//import lombok.extern.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Consumer;

@Service
public class OrderEventConsumer {

//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void handleOrderEvent(OrderCreatedEvent event){
//        System.out.println("Received Order Event: "+event);
//
//    }
    private final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);

    @Bean
    public Consumer<OrderCreatedEvent> orderCreated() {
        return event -> {
            logger.info("Received order created event for order: {}",event.getOrderId());
            logger.info("Received order created event for order: {}",event.getUserId());
        };
    }
}
