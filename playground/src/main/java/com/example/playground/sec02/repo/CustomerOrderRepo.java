package com.example.playground.sec02.repo;

import com.example.playground.sec02.domain.CustomerOrder;
import com.example.playground.sec02.domain.Product;
import com.example.playground.sec02.projections.OrderDetails;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface CustomerOrderRepo extends ReactiveCrudRepository<CustomerOrder, UUID> {
    @Query("""
    SELECT
        p.*
    FROM
        customer c
    INNER JOIN customer_order co ON c.id = co.customer_id
    INNER JOIN product p ON co.product_id = p.id
    WHERE
        c.name = :name
""")
    Flux<Product> findByName(String name);

    @Query("""
        SELECT co.order_id,
               c.name as customer_name,
               p.description as product_name,
               co.amount,
               co.order_date
        FROM customer_order co JOIN product p ON co.product_id = p.id
        JOIN customer c ON co.customer_id = c.id
        WHERE p.description  = :desc
        ORDER BY co.amount DESC
               
""")
    Flux<OrderDetails> findOrderDetailsByProduct(String desc);
}
