package com.kanga.kickrushapi.api;

import com.kanga.kickrushapi.api.dto.ProfileResponse;
import com.kanga.kickrushapi.api.dto.RegisterRequest;
import com.kanga.kickrushapi.api.dto.RegisterResponse;
import com.kanga.kickrushapi.security.AuthContext;
import com.kanga.kickrushapi.security.AuthUser;
import com.kanga.kickrushapi.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return memberService.register(request);
    }

    @GetMapping("/my/profile")
    public ProfileResponse profile() {
        AuthUser user = AuthContext.get();
        return memberService.profile(user.memberId());
    }
}
