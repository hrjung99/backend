package swyp.swyp6_team7.member.dto;

import org.springframework.beans.factory.annotation.Value;

public class UserRequestDto {
    private String email;       // user_email
    private String password;    // user_pw
    private String name;   // user_name
    private String gender;      // user_gender (M/F)
    private String agegroup;   // user_age_group

    // 관리자로 가입할 떄 사용하는 키
    @Value("${custom.admin-secret-key}")
    private String adminSecretKey;

    // 기본 생성자
    public UserRequestDto() {
    }

    public UserRequestDto(String email, String password, String name, String gender, String agegroup) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.agegroup = agegroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgegroup() {
        return agegroup;
    }

    public void setAgegroup(String agegroup) {
        this.agegroup = agegroup;
    }

    public String getAdminSecretKey() {
        return adminSecretKey;
    }

    public void setAdminSecretKey(String adminSecretKey) {
        this.adminSecretKey = adminSecretKey;
    }
}
