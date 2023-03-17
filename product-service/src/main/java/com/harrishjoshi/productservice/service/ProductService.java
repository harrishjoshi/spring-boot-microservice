package com.harrishjoshi.productservice.service;

import com.harrishjoshi.productservice.dto.ProductRequest;
import com.harrishjoshi.productservice.dto.ProductResponse;
import com.harrishjoshi.productservice.model.Product;
import com.harrishjoshi.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        // map product request values to new product object
        var product = new Product();
        BeanUtils.copyProperties(productRequest, product);

        productRepository.save(product);
        log.info("Product {} is saved.", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        var products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        // map product to product response
        var productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        
        return productResponse;
    }
}
