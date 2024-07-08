package miniProject.board.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.MemberDto;
import miniProject.board.entity.Member;
import miniProject.board.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public MemberDto.Info findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);

        return MemberDto.Info.fromMember(member);
    }

    public Member findMemberDao(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Override
    public List<MemberDto.Info> findMembers() {
        List<Member> memberList = memberRepository.findAll();

        return memberList.stream()
                .map(MemberDto.Info::fromMember)
                .toList();
    }
}
