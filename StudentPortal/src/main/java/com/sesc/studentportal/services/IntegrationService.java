package com.sesc.studentportal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hilla.BrowserCallable;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the Service used to make API calls to different Services using WebClient.
 * The configuration of the endpoint is defined in [EndpointConfig].
 * Properties are added into application.properties file.
 *
 * @see com.sesc.studentportal.misc.EndpointConfig
 */
@Service
@BrowserCallable
@PermitAll
public class IntegrationService {

    // The WebClient Builder used to make API calls to different Services.
    private final WebClient.Builder webClientBuilder;

    // CONSTRUCTOR //
    public IntegrationService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    // LIBRARY ENDPOINTS //
    @Value("${endpoint.library-host}")
    private String libraryHost;

    @Value("${endpoint.library-create-student}")
    private String libraryCreateStudent;

    // FINANCE ENDPOINTS //
    @Value("${endpoint.finance-host}")
    private String financeHost;

    @Value("${endpoint.finance-create-student}")
    private String financeCreateStudent;

    @Value("${endpoint.finance-user-status}")
    private String financeUserStatus;

    @Value("${endpoint.finance-module-enrol}")
    private String financeModuleEnrol;

    // METHODS //

    /**
     * This method is used to create a Student in the Library Service.
     *
     * @param studentId the Unique Student Number
     */
    public void createStudentLibrary(String studentId) {
        Map<String, String> requestBody = new HashMap<>();
        // Sanitising the data to be sent to the Endpoint as requested by Documentation
        requestBody.put("studentId", studentId);
        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting request body to JSON");
        }
        // Sending the HTTP POST request to the Library Service
        webClientBuilder.baseUrl(libraryHost)
                .build()
                .post()
                .uri(libraryCreateStudent)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody) // The JSON body
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> {
                    // We could implement a callback below to handle the response if needed.
                    System.out.println("Library Service Response: " + response);
                });
    }
}
