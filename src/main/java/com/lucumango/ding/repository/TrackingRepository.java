package com.lucumango.ding.repository;

import com.lucumango.ding.model.TrackingEvent;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

public class TrackingRepository {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public TrackingRepository() {
        // Initialize with default client for Lambda environment
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(System.getenv("AWS_REGION"))) // Automatically picked up in Lambda
                .build();
        this.tableName = System.getenv().getOrDefault("TABLE_NAME", "EmailTrackingEvents");
    }

    // For testing purposes
    public TrackingRepository(DynamoDbClient dynamoDbClient, String tableName) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
    }

    public void saveEvent(TrackingEvent event) {
        Map<String, AttributeValue> item = new HashMap<>();
        
        item.put("id", AttributeValue.builder().s(event.getId()).build());
        item.put("trackingId", AttributeValue.builder().s(event.getTrackingId()).build());
        item.put("timestamp", AttributeValue.builder().s(event.getTimestamp()).build());
        
        if (event.getIpAddress() != null && !event.getIpAddress().isEmpty()) {
            item.put("ipAddress", AttributeValue.builder().s(event.getIpAddress()).build());
        }
        
        if (event.getUserAgent() != null && !event.getUserAgent().isEmpty()) {
            item.put("userAgent", AttributeValue.builder().s(event.getUserAgent()).build());
        }

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }
}
