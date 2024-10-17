package com.example.playground.sec04.mapper;

import com.example.playground.sec04.dto.CustomerRequest;
import com.example.playground.sec04.dto.CustomerResponse;
import com.example.playground.sec04.entity.Customer;

public class CustomerMapper {
    public static Customer toEntity(CustomerRequest request){
        return Customer.builder()
                .email(request.email())
                .name(request.name())
                .build();
    }

    public static CustomerResponse toResponse(Customer customer){
        return CustomerResponse
                .builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .name(customer.getName())
                .build();
    }
}
