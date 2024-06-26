package miniProject.board.service;

import miniProject.board.dto.MemberDto;

import java.util.List;

public interface MemberService {
    MemberDto.Info signUp(MemberDto.Create createMemberDto);

    MemberDto.Session login(MemberDto.Login loginMemberDto);

    MemberDto.Info findMember(Long memberId);

    List<MemberDto.Info> findMembers();
}