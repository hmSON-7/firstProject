package miniProject.board.service;

import miniProject.board.dto.MemberAddDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.dto.MemberLoginDto;
import miniProject.board.dto.MemberSessionDto;

import java.util.List;

public interface MemberService {
    MemberDto signUp(MemberAddDto memberAddDto);

    MemberSessionDto login(MemberLoginDto memberLoginDto);

    MemberDto findMember(Long memberId);

    List<MemberDto> findMembers();
}