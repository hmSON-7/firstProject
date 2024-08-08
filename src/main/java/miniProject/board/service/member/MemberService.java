package miniProject.board.service.member;

import miniProject.board.dto.MemberDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public interface MemberService {
    MemberDto.Info findMember(Long memberId);

    List<MemberDto.Info> findMembers();

    void deleteMember(Long memberId);
}