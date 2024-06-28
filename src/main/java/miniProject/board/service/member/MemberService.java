package miniProject.board.service.member;

import miniProject.board.dto.MemberDto;

import java.util.List;

public interface MemberService {
    MemberDto.Info findMember(Long memberId);

    List<MemberDto.Info> findMembers();
}