package org.dcode.artificialswbackend.mypage.dto;

public class MyPagePrivateEditRequestDto {
    private String phone;
    private String password;
    
    public MyPagePrivateEditRequestDto() {}
    
    public MyPagePrivateEditRequestDto(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}