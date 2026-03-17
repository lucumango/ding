package com.lucumango.ding.model;

public class TrackingEvent {
    private String id;
    private String trackingId;
    private String timestamp;
    private String ipAddress;
    private String userAgent;

    public TrackingEvent() {
    }

    public TrackingEvent(String id, String trackingId, String timestamp, String ipAddress, String userAgent) {
        this.id = id;
        this.trackingId = trackingId;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return "TrackingEvent{" +
                "id='" + id + '\'' +
                ", trackingId='" + trackingId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
