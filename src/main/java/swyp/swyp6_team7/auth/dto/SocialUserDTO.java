package swyp.swyp6_team7.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.member.entity.SocialProvider;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserDTO {
    private String socialNumber; // 소셜 고유 ID
    private String email;
    private String name;
    private String gender;
    private String ageGroup;
    private String provider; // kakao, naver, google


}
