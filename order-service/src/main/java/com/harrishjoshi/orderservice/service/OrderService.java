package com.harrishjoshi.orderservice.service;

import com.harrishjoshi.orderservice.dto.OrderLineItemsDto;
import com.harrishjoshi.orderservice.dto.OrderRequest;
import com.harrishjoshi.orderservice.model.Order;
import com.harrishjoshi.orderservice.model.OrderLineItems;
import com.harrishjoshi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        var orderLineItems = orderRequest.getOrderLineItems()
                .stream()
                .map(this::mapOrderLineItemsDtoToObject)
                .toList();
        order.setOrderLineItems(orderLineItems);
        orderRepository.save(order);
        log.info("Order {} is placed successfully.", order.getOrderNumber());
    }

    private OrderLineItems mapOrderLineItemsDtoToObject(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());

        return orderLineItems;
    }
}