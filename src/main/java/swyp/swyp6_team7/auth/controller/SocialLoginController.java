package swyp.swyp6_team7.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.dto.SocialUserDTO;
import swyp.swyp6_team7.auth.service.SocialLoginService;
import swyp.swyp6_team7.member.entity.Users;

@RestController
@RequestMapping("/login/oauth")
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    public SocialLoginController(SocialLoginService socialLoginService) {
        this.socialLoginService = socialLoginService;
    }

    @GetMapping("{provider}")
    public Users loginWithProvider(@PathVariable String provider, @RequestParam String code, @RequestParam String state) {
        return socialLoginService.handleSocialLogin(provider, code, state);
    }
}
