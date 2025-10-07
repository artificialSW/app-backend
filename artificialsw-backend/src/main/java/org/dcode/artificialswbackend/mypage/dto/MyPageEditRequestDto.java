package org.dcode.artificialswbackend.mypage.dto;

import java.time.LocalDate;

public class MyPageEditRequestDto {
    private String name;
    private LocalDate birth;
    private String familyType;

    public MyPageEditRequestDto() {}

    public MyPageEditRequestDto(String name, LocalDate birth, String familyType) {
        this.name = name;
        this.birth = birth;
        this.familyType = familyType;
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

    public String getFamilyType() {
        return familyType;
    }

    public void setFamilyType(String familyType) {
        this.familyType = familyType;
    }
}