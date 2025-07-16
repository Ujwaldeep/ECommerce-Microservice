package com.ecommerce.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class FallBackController {
    @GetMapping("/fallback/products")
    public ResponseEntity<List<String>> productsFallback(){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).
                body(Collections.singletonList("Product Service is not available, Please try after some time"));
    }

    @GetMapping("/fallback/users")
    public ResponseEntity<List<String>> usersFallback(){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).
                body(Collections.singletonList("User Service is not available, Please try after some time"));
    }

    @GetMapping("/fallback/orders")
    public ResponseEntity<List<String>> ordersFallback(){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).
                body(Collections.singletonList("Order Service is not available, Please try after some time"));
    }
}
