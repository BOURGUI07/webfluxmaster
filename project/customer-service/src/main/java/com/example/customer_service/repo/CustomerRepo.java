package com.example.customer_service.repo;

import com.example.customer_service.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends ReactiveCrudRepository<Customer, Integer> {

}
