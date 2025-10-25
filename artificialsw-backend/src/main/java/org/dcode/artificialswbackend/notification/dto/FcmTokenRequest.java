package org.dcode.artificialswbackend.notification.dto;

public class FcmTokenRequest {
    private String token;
    private String deviceType; // "android", "ios"
    
    public FcmTokenRequest() {}
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getDeviceType() {
        return deviceType;
    }
    
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}