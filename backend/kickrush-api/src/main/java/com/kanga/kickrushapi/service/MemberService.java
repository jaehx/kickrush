package com.kanga.kickrushapi.service;

import com.kanga.kickrushapi.api.dto.ProfileResponse;
import com.kanga.kickrushapi.api.dto.RegisterRequest;
import com.kanga.kickrushapi.api.dto.RegisterResponse;
import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.mock.Member;
import com.kanga.kickrushapi.mock.MemberRole;
import com.kanga.kickrushapi.mock.MockStore;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    public RegisterResponse register(RegisterRequest request) {
        MockStore.findMemberByEmail(request.email()).ifPresent(member -> {
            throw MockStore.apiError(ErrorCode.DUPLICATE_EMAIL, "이미 사용 중인 이메일입니다.");
        });
        long newId = MockStore.nextMemberId();
        Member member = new Member(newId, request.email(), request.password(), request.name(), MemberRole.USER);
        MockStore.MEMBERS.add(member);
        return new RegisterResponse(member.id(), member.email(), member.name());
    }

    public ProfileResponse profile(Long memberId) {
        Member member = MockStore.getMember(memberId);
        return new ProfileResponse(member.id(), member.email(), member.name(), member.role().name());
    }
}
