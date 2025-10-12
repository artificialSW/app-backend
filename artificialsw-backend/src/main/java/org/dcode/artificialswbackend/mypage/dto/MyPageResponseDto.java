package org.dcode.artificialswbackend.mypage.dto;

import java.time.LocalDate;

public class MyPageResponseDto {
    private String name;
    private LocalDate birth;
    private String family_type;
    private String family_code;

    public MyPageResponseDto() {}

    public MyPageResponseDto(String name, LocalDate birth, String family_type, String family_code) {
        this.name = name;
        this.birth = birth;
        this.family_type = family_type;
        this.family_code = family_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getFamily_type() {
        return family_type;
    }

    public void setFamily_type(String family_type) {
        this.family_type = family_type;
    }

    public String getFamily_code() {
        return family_code;
    }

    public void setFamily_code(String family_code) {
        this.family_code = family_code;
    }
}