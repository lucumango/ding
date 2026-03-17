package com.lucumango.ding.service;

import com.lucumango.ding.model.TrackingEvent;
import com.lucumango.ding.repository.TrackingRepository;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public class TrackingService {

    private final TrackingRepository trackingRepository;

    // A 1x1 transparent GIF pixel base64 encoded
    private static final String TRANSPARENT_PIXEL_BASE64 = "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";
    public static final byte[] TRANSPARENT_PIXEL_BYTES = Base64.getDecoder().decode(TRANSPARENT_PIXEL_BASE64);

    public TrackingService() {
        this.trackingRepository = new TrackingRepository();
    }

    public TrackingService(TrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    /**
     * Records the tracking event and returns a transparent 1x1 pixel.
     * @param trackingId The ID embedded in the email
     * @param ipAddress User's IP address extracted from the request
     * @param userAgent User's User-Agent string
     * @return Byte array of a transparent 1x1 pixel image
     */
    public byte[] trackAndGetImage(String trackingId, String ipAddress, String userAgent) {
        if (trackingId == null || trackingId.isEmpty()) {
            trackingId = "unknown";
        }

        TrackingEvent event = new TrackingEvent(
                UUID.randomUUID().toString(),
                trackingId,
                Instant.now().toString(),
                ipAddress,
                userAgent
        );

        try {
            trackingRepository.saveEvent(event);
            System.out.println("Successfully recorded tracking event: " + event.toString());
        } catch (Exception e) {
            // We shouldn't fail the image payload delivery even if DB fails
            System.err.println("Failed to save tracking event to DynamoDB: " + e.getMessage());
            e.printStackTrace();
        }

        return TRANSPARENT_PIXEL_BYTES;
    }
}
