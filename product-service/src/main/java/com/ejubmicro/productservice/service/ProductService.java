package com.ejubmicro.productservice.service;

import com.ejubmicro.productservice.dto.ProductRequest;
import com.ejubmicro.productservice.dto.ProductResponse;
import com.ejubmicro.productservice.model.Product;
import com.ejubmicro.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Service //as this is service I added this annotation
@RequiredArgsConstructor  //when I add this annotation I don't have to hold constructor written inside of a class
@Slf4j //for logging
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse getProductByName(String name) {
        Product product = productRepository.findAll()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapToProductResponse(product);
    }

    public void createProduct(ProductRequest productRequest){

        //creation of an object of Product class
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        //saving created product inside of a database
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
       return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }


}
