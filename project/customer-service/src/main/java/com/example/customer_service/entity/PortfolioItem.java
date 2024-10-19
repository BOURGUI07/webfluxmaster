package com.example.customer_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="portfolio_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class PortfolioItem {
    @Id
    private Integer id;
    private Integer customerId;
    private Ticker ticker;
    private Integer quantity;

    public Integer getQuantity(){
        return (quantity==null)?0:quantity;
    }
}
