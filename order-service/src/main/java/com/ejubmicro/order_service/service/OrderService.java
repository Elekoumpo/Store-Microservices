package com.ejubmicro.order_service.service;

import com.ejubmicro.order_service.dto.OrderLineItemsDto;
import com.ejubmicro.order_service.dto.OrderRequest;
import com.ejubmicro.order_service.model.Order;
import com.ejubmicro.order_service.model.OrderLineItems;
import com.ejubmicro.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        //Call inventory service (WebClient chosen over RestTemplate) - place order if product is in stock
            Boolean result = webClient.get() //in Inventory Controller I mapped the Get
                    .uri("http://localhost:8082/api/inventory")
                    .retrieve()
                    .bodyToMono(Boolean.class) //by default client will make async request, but I need to make it sync
                    .block();

        if(result) {
        orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }


        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems =  new OrderLineItems();
        orderLineItems.setPrice(orderLineItems.getPrice());
        orderLineItems.setQuantity(orderLineItems.getQuantity());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItems;
    }

}
