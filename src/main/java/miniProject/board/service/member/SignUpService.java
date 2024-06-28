package miniProject.board.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.auth.constants.Role;
import miniProject.board.dto.MemberDto;
import miniProject.board.entity.Member;
import miniProject.board.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberDto.Info signUp(MemberDto.Create createMemberDto) {
        Member member = Member.createMember(createMemberDto.getUsername(),
                passwordEncoder.encode(createMemberDto.getPassword()),
                createMemberDto.getEmail(),
                Role.ROLE_USER
        );

        memberRepository.save(member);

        return MemberDto.Info.fromMember(member);
    }
}
