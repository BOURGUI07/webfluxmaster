package com.example.playground.sec03.controller;

import com.example.playground.sec03.dto.CustomerRequest;
import com.example.playground.sec03.dto.CustomerResponse;
import com.example.playground.sec03.dto.PaginatedCustomerResponse;
import com.example.playground.sec03.service.CustomerService;
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
    public Mono<ResponseEntity<CustomerResponse>> findById(@PathVariable Integer customerId) {
        return service.findById(customerId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/paginated")
    public Mono<ResponseEntity<PaginatedCustomerResponse>> findAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "3") Integer size){
        return service.findAllPaginated(page,size)
                .map(ResponseEntity::ok);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CustomerResponse> findAll() {
        return service.findAll();
    }

    @PostMapping()
    public Mono<ResponseEntity<CustomerResponse>> save(@RequestBody Mono<CustomerRequest> customer) {
        return service.save(customer)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{customerId}")
    public Mono<ResponseEntity<CustomerResponse>> update(@PathVariable Integer customerId, @RequestBody Mono<CustomerRequest> customer) {
        return service.update(customerId, customer)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{customerId}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Integer customerId) {
        return service.delete(customerId)
                .filter(b-> b)
                .map(x->ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }



}
