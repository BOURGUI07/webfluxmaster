package com.example.aggregator_service.client;

import com.example.aggregator_service.domain.Ticker;
import com.example.aggregator_service.dto.PriceUpdate;
import com.example.aggregator_service.dto.StockPriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class StockServiceClient {
    private final WebClient client;
    private Flux<PriceUpdate> priceUpdateFlux;

    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return client
                .get()
                .uri("/stock/{ticker}",ticker)
                .retrieve()
                .bodyToMono(StockPriceResponse.class);
    }

    private Flux<PriceUpdate> getPriceUpdates() {
        return client
                .get()
                .uri("/stock/price-stream")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(PriceUpdate.class)
                .retryWhen(retry())
                .cache(1); //GET THE LAST PRICE UPDATE
    }

    public Flux<PriceUpdate> getPriceUpdateFlux() {
        if(Objects.isNull(priceUpdateFlux)) {
            priceUpdateFlux = getPriceUpdates();
        }
        return priceUpdateFlux;
    }

    private Retry retry(){
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
                .doBeforeRetry(rs-> log.info("STOCK SERVICE PRICE UPDATE STREAM FAILED: {}, RETRYING",rs.failure().getMessage()));
    }
}
