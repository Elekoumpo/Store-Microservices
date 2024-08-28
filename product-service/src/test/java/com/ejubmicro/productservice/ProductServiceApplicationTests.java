package com.ejubmicro.productservice;

import com.ejubmicro.productservice.dto.ProductRequest;
import com.ejubmicro.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc //I added this since @Autowired showed error (can't autowire mockmvc without this)
class ProductServiceApplicationTests {

	@Container   //so that JUnit file understands this is a MongoDB container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	//static is added in, I want to statically access the mongoDBcontainer
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dimDynamicPropertyRegistry){
	dimDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	//first parameter is used because this integration test is not using local mongo db, rather it uses docker image and it needs
		// to get the uri dinamically
	}

	@Test
	//testing the creation of the product
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String ProductRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ProductRequestString))
				.andExpect(status().isCreated());
		Assertions.assertTrue(productRepository.findAll().size() == 1);
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("iPhone 13")
				.description("iPhone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
