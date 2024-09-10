package swyp.swyp6_team7.member.dto;

public class UserRequestDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String gender;
    private String birthYear;
    private String introduce;
    //private List<TagDto> tags; // Tag 정보를 담는 리스트

    // 기본 생성자

    public UserRequestDto() {
    }

    public UserRequestDto(String email, String password, String name, String phone, String gender, String birthYear, String introduce) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.birthYear = birthYear;
        this.introduce = introduce;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
