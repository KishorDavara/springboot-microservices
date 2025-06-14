package com.microservices.product.service;

import com.microservices.product.model.Product;
import com.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(Product productRequest) {
        Product product = productRepository.save(productRequest);
        log.info("Product created successfully.");
        return product;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }
}
