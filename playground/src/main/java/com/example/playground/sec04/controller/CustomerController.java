package com.example.playground.sec04.controller;

import com.example.playground.sec04.dto.CustomerRequest;
import com.example.playground.sec04.dto.CustomerResponse;
import com.example.playground.sec04.dto.PaginatedCustomerResponse;
import com.example.playground.sec04.exceptions.ApplicationExceptions;
import com.example.playground.sec04.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @GetMapping("/{customerId}")
    public Mono<CustomerResponse> findById(@PathVariable Integer customerId) {
        return service.findById(customerId);
    }

    @GetMapping("/paginated")
    public Mono<PaginatedCustomerResponse> findAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "3") Integer size){
        return service.findAllPaginated(page,size);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CustomerResponse> findAll() {
        return service.findAll();
    }

    @PostMapping()
    public Mono<CustomerResponse> save(@RequestBody Mono<CustomerRequest> customer) {
        return service.save(customer);
    }

    @PutMapping("/{customerId}")
    public Mono<CustomerResponse> update(@PathVariable Integer customerId, @RequestBody Mono<CustomerRequest> customer) {
        return service.update(customerId, customer);
    }

    @DeleteMapping("/{customerId}")
    public Mono<Void> delete(@PathVariable Integer customerId) {
        return service.delete(customerId)
                .filter(b-> b)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .then();
    }



}
