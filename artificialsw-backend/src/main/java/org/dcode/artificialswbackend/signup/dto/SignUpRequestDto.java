package org.dcode.artificialswbackend.signup.dto;

public class SignUpRequestDto {
    private String name;
    private String phone;
    private String loginId;
    private String password;
    private String nickname;
    private String birthday; // yyyy-MM-dd 형식 문자열
    private String gender;   // 'M' 또는 'F'
    private String profilePhoto; // 이미지 경로나 URL
    private Integer age;

    public SignUpRequestDto() {}

    public SignUpRequestDto(String name, String phone, String loginId, String password, String nickname, String birthday, String gender, String profilePhoto, Integer age) {
        this.name = name;
        this.phone = phone;
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
