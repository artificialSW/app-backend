package org.dcode.artificialswbackend.notification.dto;

public class NotificationRequest {
    private String title;
    private String body;
    private String data; // JSON 문자열 또는 추가 데이터
    private Long targetUserId; // 특정 사용자에게만 보낼 경우
    private Boolean sendToFamily = false; // 가족 전체에게 보낼지 여부
    
    public NotificationRequest() {}
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public Long getTargetUserId() {
        return targetUserId;
    }
    
    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }
    
    public Boolean getSendToFamily() {
        return sendToFamily;
    }
    
    public void setSendToFamily(Boolean sendToFamily) {
        this.sendToFamily = sendToFamily;
    }
}