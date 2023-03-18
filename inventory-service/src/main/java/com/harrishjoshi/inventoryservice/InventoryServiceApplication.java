package com.harrishjoshi.inventoryservice;

import com.harrishjoshi.inventoryservice.model.Inventory;
import com.harrishjoshi.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        var itemsMap = new HashMap<String, Integer>() {{
            put("Laptop", 15);
            put("Mobile", 20);
            put("Watch", 0);
        }};

        return args -> {
            for (Map.Entry<String, Integer> entry : itemsMap.entrySet()) {
                Inventory inventory = new Inventory();
                inventory.setSkuCode(entry.getKey());
                inventory.setQuantity(entry.getValue());

                inventoryRepository.save(inventory);
            }
        };
    }
}