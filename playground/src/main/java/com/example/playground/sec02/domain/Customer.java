package com.example.playground.sec02.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Customer {
    @Id
    private Integer id;
    private String name;
    private String email;
}
