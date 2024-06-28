package miniProject.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.auth.constants.Role;
import miniProject.board.dto.MemberDto;
import miniProject.board.entity.Member;
import miniProject.board.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public MemberDto.Info signUp(MemberDto.Create createMemberDto) {
        Member member = Member.createMember(createMemberDto.getUsername(),
                passwordEncoder.encode(createMemberDto.getPassword()),
                createMemberDto.getEmail(),
                Role.ROLE_USER
        );

        memberRepository.save(member);

        return MemberDto.Info.fromMember(member);
    }

    @Override
    public MemberDto.Session login(MemberDto.Login loginMemberDto) {
        Optional<Member> optionalMember = memberRepository.findByUsername(loginMemberDto.getUsername());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            if (passwordEncoder.matches(loginMemberDto.getPassword(), member.getPassword())) {
                return MemberDto.Session.fromMember(member);
            }
        }

        return null;
    }

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
