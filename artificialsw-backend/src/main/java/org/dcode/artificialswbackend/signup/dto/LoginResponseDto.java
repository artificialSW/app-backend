package org.dcode.artificialswbackend.signup.dto;

public class LoginResponseDto {
    private final String token;

    public LoginResponseDto(String token, Long archiveId) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
