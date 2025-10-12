package org.dcode.artificialswbackend.signup.dto;

public class LoginRequestDto {
    private String id; // 전화번호
    private String password;

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
}
