// A_T_Service/trade-service/src/main/java/com/morganstanley/tradeservice/service/TradeConsumerService.java
package com.morganstanley.tradeservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.morganstanley.tradeservice.model.AccountDto;
import com.morganstanley.tradeservice.model.EnrichedTradeDto;
import com.morganstanley.tradeservice.model.TradeXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class TradeConsumerService {

    private static final Logger log = LoggerFactory.getLogger(TradeConsumerService.class);
    
    private final RestTemplate restTemplate;
    
    // Serializers/deserializers initialized once
    private final XmlMapper xmlMapper = new XmlMapper();
    private final ObjectMapper jsonMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT); // Premium pretty printed JSON!

    @Value("${account.service.url}")
    private String accountServiceUrl;

    // Constructor Injection (Lombok free)
    public TradeConsumerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Consumes XML messages from the Kafka 'trade-topic' topic, enriches them
     * with account details from the Account REST service, and logs the enriched JSON.
     */
    @KafkaListener(topics = "trade-topic", groupId = "trade-group")
    public void consumeTradeMessage(String xmlMessage) {
        log.info("--------------------------------------------------------------------------------");
        log.info("Received Kafka Message:\n{}", xmlMessage);

        try {
            // 1. Deserialize XML payload into TradeXml object
            TradeXml trade = xmlMapper.readValue(xmlMessage, TradeXml.class);
            log.info("Successfully deserialized Trade XML (ID: {}, Account Number: {})", 
                    trade.getTradeId(), trade.getAccountNumber());

            // 2. Query Account Service to retrieve account details
            AccountDto account = fetchAccountDetails(trade.getAccountNumber());

            // 3. Assemble and enrich the Trade Details
            EnrichedTradeDto enrichedTrade = EnrichedTradeDto.builder()
                    .tradeId(trade.getTradeId())
                    .time(trade.getTime())
                    .amount(trade.getAmount())
                    .accountNumber(trade.getAccountNumber())
                    .accountHolderName(account != null ? account.getAccountHolderName() : "UNKNOWN")
                    .currency(account != null ? account.getCurrency() : "UNKNOWN")
                    .branch(account != null ? account.getBranch() : "UNKNOWN")
                    .build();

            // 4. Output the premium enriched details in JSON format
            String jsonOutput = jsonMapper.writeValueAsString(enrichedTrade);
            
            // Highlighted print in logs as requested
            log.info("=== ENRICHED TRADE JSON OUTPUT ===");
            System.out.println(jsonOutput);
            log.info("====================================");

        } catch (JsonProcessingException e) {
            log.error("Failed to parse the XML message or generate JSON output: {}", e.getMessage());
        } catch (Exception e) {
            log.error("An unexpected error occurred during trade processing: {}", e.getMessage(), e);
        }
        log.info("--------------------------------------------------------------------------------");
    }

    /**
     * Helper method to call the account-service REST endpoint.
     */
    private AccountDto fetchAccountDetails(String accountNumber) {
        String url = accountServiceUrl + "/" + accountNumber;
        try {
            log.info("Calling Account Service: {}", url);
            return restTemplate.getForObject(url, AccountDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Account number '{}' not found in Account Service (HTTP 404)", accountNumber);
            return null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error calling Account Service for account {}: {}", accountNumber, e.getStatusCode());
            return null;
        } catch (ResourceAccessException e) {
            log.error("Account Service is unreachable. Ensure it is running on port 8081. Details: {}", e.getMessage());
            return null;
        }
    }
}
