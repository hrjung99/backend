package swyp.swyp6_team7.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    // 추가정보
    private Integer userNumber;
    private String email;
    private String gender;
    private String ageGroup;
    private Set<String> preferredTags;
}
