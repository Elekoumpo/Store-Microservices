package com.ejubmicro.productservice.repository;

import com.ejubmicro.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String>{

}
