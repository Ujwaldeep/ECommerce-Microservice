package com.ecommerce.product.controllers;


import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(("/api/products"))
public class ProductController {

    private final ProductService productService;

//    @GetMapping
//    public ResponseEntity<String> simulateFailure(@RequestBody ProductRequest product) {
//        return new ResponseEntity.ok("Product service is ok");
//    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest product) {
        return new ResponseEntity<ProductResponse>(productService.createProduct(product), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
        Optional<ProductResponse> productResponse = productService.getByProductId(id);
        return productResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,@RequestBody ProductRequest product) {
        return productService.upateProduct(id,product)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        return deleted?ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
//        return ResponseEntity.ok(productService.searchProducts(keyword));
//    }
}
