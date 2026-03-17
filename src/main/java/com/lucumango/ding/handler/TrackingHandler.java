package com.lucumango.ding.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lucumango.ding.service.TrackingService;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * AWS Lambda handler mapping to an APIGatewayProxyRequestEvent
 * Will expect GET /track/{id}
 */
public class TrackingHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final TrackingService trackingService;

    public TrackingHandler() {
        this.trackingService = new TrackingService();
    }

    public TrackingHandler(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        context.getLogger().log("Received request: " + input.getPath());

        String trackingId = "unknown";
        if (input.getPathParameters() != null && input.getPathParameters().containsKey("id")) {
            trackingId = input.getPathParameters().get("id");
            // If the URL is like /track/my-email-123.gif we clean up the .gif extension if present
            if (trackingId.endsWith(".gif") || trackingId.endsWith(".png")) {
                trackingId = trackingId.substring(0, trackingId.lastIndexOf('.'));
            }
        }

        String ipAddress = "";
        String userAgent = "";

        if (input.getRequestContext() != null && input.getRequestContext().getIdentity() != null) {
            ipAddress = input.getRequestContext().getIdentity().getSourceIp();
            userAgent = input.getRequestContext().getIdentity().getUserAgent();
        }

        // Generate the event
        byte[] imageBytes = trackingService.trackAndGetImage(trackingId, ipAddress, userAgent);

        // Required headers to render as a 1x1 image and avoid client caching
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "image/gif");
        headers.put("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.put("Pragma", "no-cache");
        headers.put("Expires", "0");

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(headers)
                .withIsBase64Encoded(true)
                .withBody(Base64.getEncoder().encodeToString(imageBytes));
    }
}
