package org.dcode.artificialswbackend.signup.dto;

public class LoginRequestDto {
    private String id; // 전화번호
    private String password;
    private String token; // FCM 토큰 (선택사항)

    public LoginRequestDto() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
