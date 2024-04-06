package com.sesc.libraryservice.integration;

import com.sesc.libraryservice.dto.Invoice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class IntegrationService {

    // The WebClient Builder used to make API calls to different Services.
    private final WebClient.Builder webClientBuilder;

    // CONSTRUCTOR //
    public IntegrationService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    // ENDPOINTS //
    @Value("${endpoint.finance-host}")
    private String financeHost;

    @Value("${endpoint.finance-send-invoice}")
    private String financeSendInvoice;

    // METHODS //

    /**
     * This method sends an invoice to the finance service.
     *
     * @param invoice the invoice to send
     * @return the invoice sent
     */
    public Invoice sendInvoice(Invoice invoice) {
        return webClientBuilder.baseUrl(financeHost)
                .build()
                .post()
                .uri(financeSendInvoice)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invoice)
                .retrieve()
                .bodyToMono(Invoice.class)
                .block();
    }
}
