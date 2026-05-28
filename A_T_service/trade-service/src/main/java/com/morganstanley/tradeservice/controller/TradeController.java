package com.morganstanley.tradeservice.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.morganstanley.tradeservice.exception.InvalidXmlException;
import com.morganstanley.tradeservice.exception.KafkaPublishException;
import com.morganstanley.tradeservice.model.TradeXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/trades")
public class TradeController {

    private static final Logger log = LoggerFactory.getLogger(TradeController.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final XmlMapper xmlMapper = new XmlMapper();
    private static final String TOPIC = "trade-topic";

    // Constructor Injection (Lombok free)
    public TradeController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * POST /trades/publish
     * Accepts XML trade message in the request body, validates it,
     * and publishes it to the local Kafka 'trade-topic'.
     */
    @PostMapping(
            value = "/publish",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE, MediaType.TEXT_PLAIN_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> publishTrade(@RequestBody String xmlPayload) {
        log.info("Received request to publish trade XML to Kafka.");
        
        if (xmlPayload == null || xmlPayload.trim().isEmpty()) {
            throw new InvalidXmlException("Request body XML is empty");
        }

        TradeXml trade;
        try {
            // 1. Parse and validate XML format
            trade = xmlMapper.readValue(xmlPayload, TradeXml.class);
            
            // 2. Validate essential fields
            if (trade.getTradeId() == null || trade.getTradeId().trim().isEmpty()) {
                throw new InvalidXmlException("Missing mandatory XML element: tradeId");
            }
            if (trade.getAccountNumber() == null || trade.getAccountNumber().trim().isEmpty()) {
                throw new InvalidXmlException("Missing mandatory XML element: accountNumber");
            }
            
            log.info("XML parsed successfully. Valid Trade ID detected: {}", trade.getTradeId());

        } catch (InvalidXmlException ex) {
            throw ex;
        } catch (Exception ex) {
            log.warn("Malformed XML parse error: {}", ex.getMessage());
            throw new InvalidXmlException("Invalid or malformed XML syntax: " + ex.getMessage());
        }

        // 3. Publish XML message to Kafka topic
        try {
            log.info("Publishing raw XML to topic '{}' with key '{}'", TOPIC, trade.getTradeId());
            
            // Synchronous send using get() to intercept publishing exceptions instantly
            kafkaTemplate.send(TOPIC, trade.getTradeId(), xmlPayload).get();
            
            log.info("Successfully published Trade '{}' to Kafka", trade.getTradeId());

        } catch (Exception ex) {
            log.error("Failed to publish message to Kafka topic '{}': {}", TOPIC, ex.getMessage());
            throw new KafkaPublishException("Message broker is unavailable. Failed to publish trade XML.", ex);
        }

        // 4. Return clean, user-friendly JSON response for demo
        Map<String, String> response = new HashMap<>();
        response.put("message", "Trade message published successfully");
        response.put("tradeId", trade.getTradeId());
        response.put("topic", TOPIC);

        return ResponseEntity.ok(response);
    }
}
