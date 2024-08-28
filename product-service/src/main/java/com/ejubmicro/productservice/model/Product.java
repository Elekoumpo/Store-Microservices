package com.ejubmicro.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "product")   //annotate that Product is a MongoDB document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {
    @Id    //serves to annotate that this id is unique identifier for our products (not other product can have some id)
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
