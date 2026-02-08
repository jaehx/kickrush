package com.kanga.kickrushapi.member;

import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.member.dto.ProfileResponse;
import com.kanga.kickrushapi.member.dto.RegisterRequest;
import com.kanga.kickrushapi.member.dto.RegisterResponse;
import com.kanga.kickrushapi.mock.Member;
import com.kanga.kickrushapi.mock.MemberRole;
import com.kanga.kickrushapi.store.Store;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final Store store;

    public MemberService(Store store) {
        this.store = store;
    }

    public RegisterResponse register(RegisterRequest request) {
        store.findMemberByEmail(request.email()).ifPresent(member -> {
            throw store.apiError(ErrorCode.DUPLICATE_EMAIL, "이미 사용 중인 이메일입니다.");
        });
        long newId = store.nextMemberId();
        Member member = new Member(newId, request.email(), request.password(), request.name(), MemberRole.USER);
        store.addMember(member);
        return new RegisterResponse(member.id(), member.email(), member.name());
    }

    public ProfileResponse profile(Long memberId) {
        Member member = store.getMember(memberId);
        return new ProfileResponse(member.id(), member.email(), member.name(), member.role().name());
    }
}
