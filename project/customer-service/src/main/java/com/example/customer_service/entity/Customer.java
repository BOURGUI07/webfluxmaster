package com.example.customer_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Customer {
    @Id
    private Integer id;
    private String name;
    private Integer balance;
}
