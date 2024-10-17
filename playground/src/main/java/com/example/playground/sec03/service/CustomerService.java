package com.example.playground.sec03.service;

import com.example.playground.sec03.dto.CustomerRequest;
import com.example.playground.sec03.dto.CustomerResponse;
import com.example.playground.sec03.dto.PaginatedCustomerResponse;
import com.example.playground.sec03.mapper.CustomerMapper;
import com.example.playground.sec03.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepo repo;

    public Mono<CustomerResponse> findById(Integer id) {
        return repo.findById(id)
                .map(CustomerMapper::toResponse);
    }

    public Flux<CustomerResponse> findAll() {
        return repo.findAll()
                .map(CustomerMapper::toResponse);
    }

    public Mono<PaginatedCustomerResponse> findAllPaginated(Integer page, Integer size) {
        return repo.findBy(PageRequest.of(page-1,size))
                .map(CustomerMapper::toResponse)
                .collectList()
                .zipWith(repo.count())
                .map(x-> PaginatedCustomerResponse.builder()
                        .response(x.getT1())
                        .count(x.getT2())
                        .build());
    }

    public Mono<CustomerResponse> save(Mono<CustomerRequest> request) {
        return request.map(CustomerMapper::toEntity)
                .flatMap(repo::save)
                .map(CustomerMapper::toResponse);
    }

    public Mono<CustomerResponse> update(Integer id, Mono<CustomerRequest> request) {
        return repo.findById(id)
                .zipWith(request)
                .flatMap(x -> {
                    var customer = x.getT1();
                    var requestTuple = x.getT2();
                    customer.setEmail(requestTuple.email())
                            .setName(requestTuple.name());
                    return repo.save(customer);
                })
                .map(CustomerMapper::toResponse);
    }

    public Mono<Boolean> delete(Integer id) {
        return repo.deleteCustomerById(id);
    }
}
