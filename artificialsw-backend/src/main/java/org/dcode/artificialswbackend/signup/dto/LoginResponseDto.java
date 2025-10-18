package org.dcode.artificialswbackend.signup.dto;

import lombok.Setter;

public class LoginResponseDto {
    private final String token;
    @Setter
    private Long archiveId;

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
