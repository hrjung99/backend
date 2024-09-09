package swyp.swyp6_team7.member.controller;

import org.springframework.http.ResponseEntity;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody Users user) {
        memberService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
