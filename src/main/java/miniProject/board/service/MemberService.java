package miniProject.board.service;

import miniProject.board.dto.MemberAddDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.dto.MemberLoginDto;
import miniProject.board.entity.Member;

import java.util.List;

public interface MemberService {
    MemberDto signUp(MemberAddDto memberAddDto);

    MemberDto login(MemberLoginDto memberLoginDto);

    MemberDto findMember(Long memberId);

    List<MemberDto> findMembers();

    MemberDto convertToDto(Member member);

    Member convertToDao(MemberAddDto memberAddDto);
}
