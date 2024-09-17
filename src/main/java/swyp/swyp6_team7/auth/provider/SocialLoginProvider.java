package swyp.swyp6_team7.auth.provider;

import swyp.swyp6_team7.auth.dto.SocialUserDTO;

public interface SocialLoginProvider {
    boolean supports(String provider);
    SocialUserDTO getUserInfo(String code, String state);
}
