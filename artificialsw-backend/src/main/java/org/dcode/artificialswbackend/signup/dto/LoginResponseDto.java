package org.dcode.artificialswbackend.signup.dto;

public class LoginResponseDto {
    private final String token;
    private final Long archiveId;

    public LoginResponseDto(String token, Long archiveId) {
        this.token = token;
        this.archiveId = archiveId;
    }

    public String getToken() {
        return token;
    }

    public Long getArchiveId() {
        return archiveId;
    }
}
