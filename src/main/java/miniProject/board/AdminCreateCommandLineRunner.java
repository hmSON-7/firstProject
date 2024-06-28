package miniProject.board;

import lombok.RequiredArgsConstructor;
import miniProject.board.auth.constants.Role;
import miniProject.board.entity.Admin;
import miniProject.board.entity.Member;
import miniProject.board.repository.AdminRepository;
import miniProject.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 관리자 계정이 존재 하지 않는 경우 관리자 계정을 생성하는 CommandLineRunner 구현체입니다.
 * 설정 파일에서 관리자 이름과 비밀번호를 읽어옵니다.
 * 이미 관리자 계정이 존재하는 경우 관리자를 생성하지 않습니다.
 * </pre>
 */
@Component
@RequiredArgsConstructor
@PropertySource("classpath:application-admin.properties")
public class AdminCreateCommandLineRunner implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 관리자 이름
    @Value("${admin.name}")
    private String name;

    // 관리자 비밀번호
    @Value("${admin.password}")
    private String password;

    /**
     * <pre>
     * 관리자 계정이 존재하는지 확인, 존재 하지 않을 경우 새로운 관리자 계정을 생성
     * 비밀번호는 암호화 하여 저장
     * </pre>
     */
    @Override
    public void run(String... args) throws Exception {
        long count = memberRepository.count();

        if (count == 0) { // 관리자 계정이 없는 경우

            memberRepository.save(Member.createMemberWithoutEmail(name,
                    passwordEncoder.encode(password),
                    Role.ROLE_ADMIN));
        }
    }
}