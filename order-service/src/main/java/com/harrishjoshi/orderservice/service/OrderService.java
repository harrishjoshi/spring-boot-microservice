package com.harrishjoshi.orderservice.service;

import com.harrishjoshi.orderservice.dto.InventoryResponse;
import com.harrishjoshi.orderservice.dto.OrderLineItemsDto;
import com.harrishjoshi.orderservice.dto.OrderRequest;
import com.harrishjoshi.orderservice.model.Order;
import com.harrishjoshi.orderservice.model.OrderLineItems;
import com.harrishjoshi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        var orderLineItems = orderRequest.getOrderLineItems()
                .stream()
                .map(this::mapOrderLineItemsDtoToObject)
                .toList();
        order.setOrderLineItems(orderLineItems);

        // call inventory service and place order if product is in stock else throw exception
        var skuCodes = order.getOrderLineItems()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        var productsInStockArr = webClient.get()
                .uri(
                        "http://localhost:8082/api/v1/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        var allProductsInStock = productsInStockArr != null &&
                Arrays.stream(productsInStockArr)
                        .allMatch(InventoryResponse::isInStock);

        if (Boolean.TRUE.equals(allProductsInStock)) {
            orderRepository.save(order);
            log.info("Order {} is placed successfully.", order.getOrderNumber());
        } else throw new IllegalArgumentException("Product is not in stock, please try again later.");
    }

    private OrderLineItems mapOrderLineItemsDtoToObject(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());

        return orderLineItems;
    }
}