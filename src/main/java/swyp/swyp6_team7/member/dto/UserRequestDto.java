package swyp.swyp6_team7.member.dto;

public class UserRequestDto {
    private String email;       // user_email
    private String password;    // user_pw
    private String firstName;   // user_first_name
    private String lastName;    // user_last_name
    private String gender;      // user_gender (M/F)
    private String birthYear;   // user_birth_year (yyyy)
    private String phone;       // user_phone (전화번호)

    // 기본 생성자
    public UserRequestDto() {
    }

    public UserRequestDto(String email, String password, String firstName, String lastName, String gender, String birthYear, String phone) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthYear = birthYear;
        this.phone = phone;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
