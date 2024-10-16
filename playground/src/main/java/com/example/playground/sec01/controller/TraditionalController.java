package com.example.playground.sec01.controller;

import com.example.playground.sec01.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/traditional")
public class TraditionalController {
    private RestClient restClient = RestClient.builder()
            .baseUrl("http://localhost:7070")
            .build();

    @GetMapping("/products")
    public List<ProductDTO> getProducts() {
        var list =  restClient.get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProductDTO>>(){});
        log.info("RECEIVED RESPONSE: {}", list);
        return list;
    }
}
