package miniProject.board.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.auth.constants.Role;
import miniProject.board.dto.MemberDto;
import miniProject.board.entity.Member;
import miniProject.board.service.file.DirectoryStorageService;
import miniProject.board.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DirectoryStorageService directoryStorageService;

    public MemberDto.Info signUp(MemberDto.Create createMemberDto) {
        Member member = Member.createMember(createMemberDto.getUsername(),
                passwordEncoder.encode(createMemberDto.getPassword()),
                createMemberDto.getEmail(),
                Role.ROLE_USER
        );

        memberRepository.save(member);

        boolean created = directoryStorageService.createDir(member.getUsername());
        if(!created) {
            throw new RuntimeException("디렉토리 생성 중 오류가 발생했습니다");
        }

        return MemberDto.Info.fromMember(member);
    }
}
