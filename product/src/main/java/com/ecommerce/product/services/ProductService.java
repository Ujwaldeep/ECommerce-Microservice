package com.ecommerce.product.services;


import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.models.Product;
import com.ecommerce.product.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product,productRequest);
        Product savedProdut = productRepository.save(product);
        return mapToProductResponse(savedProdut);
    }

    private ProductResponse mapToProductResponse(Product savedProdut) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProdut.getId());
        productResponse.setName(savedProdut.getName());
        productResponse.setDescription(savedProdut.getDescription());
        productResponse.setPrice(savedProdut.getPrice());
        productResponse.setActive(savedProdut.getActive());
        productResponse.setCategory(savedProdut.getCategory());
        productResponse.setImageUrl(savedProdut.getImageUrl());
        productResponse.setStockQuantity(savedProdut.getStockQuantity());
        return productResponse;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());

    }

    public Optional<ProductResponse> upateProduct(Long id, ProductRequest product) {
        return productRepository.findById(id)
                .map(existingProduct ->{
                        updateProductFromRequest(existingProduct,product);
                        Product savedProduct = productRepository.save(existingProduct);
                        return mapToProductResponse(savedProduct);
                });
    }

    public List<ProductResponse> getAllProducts() {

        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public Boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getByProductId(String id) {
        return productRepository.findByIdAndActiveTrue(Long.valueOf(id))
                .map(this::mapToProductResponse);
    }
}
