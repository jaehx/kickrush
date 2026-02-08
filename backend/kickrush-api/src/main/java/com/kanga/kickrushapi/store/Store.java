package com.kanga.kickrushapi.store;

import com.kanga.kickrushapi.common.ErrorCode;
import com.kanga.kickrushapi.mock.Member;
import com.kanga.kickrushapi.mock.Order;
import com.kanga.kickrushapi.mock.Release;
import com.kanga.kickrushapi.mock.ReleaseSize;
import com.kanga.kickrushapi.mock.Shoe;

import java.util.List;
import java.util.Optional;

public interface Store {

    Optional<Shoe> findShoe(Long id);

    Optional<Release> findRelease(Long id);

    Optional<ReleaseSize> findReleaseSize(Long id);

    Optional<Member> findMemberByEmail(String email);

    Member getMember(Long id);

    List<Shoe> shoes();

    List<Release> releases();

    List<Member> members();

    List<Order> orders();

    long nextOrderId();

    long nextReleaseId();

    long nextShoeId();

    long nextMemberId();

    long nextReleaseSizeId();

    boolean isDuplicateOrder(Long memberId, Long releaseSizeId);

    void addOrder(Order order);

    void addMember(Member member);

    void addShoe(Shoe shoe);

    void addRelease(Release release);

    RuntimeException apiError(ErrorCode code, String message);
}
