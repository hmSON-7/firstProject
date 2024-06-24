package miniProject.board.service;

import miniProject.board.dto.MemberDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class MemberServiceImplTest {
    @Autowired
    MemberServiceImpl memberService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void checkSignUp() {
        // given
        MemberDto.Create createMemberDto = new MemberDto.Create();
        createMemberDto.setUsername("admin");
        createMemberDto.setPassword("123456");
        createMemberDto.setConfirmPassword("123456");
        createMemberDto.setEmail("admin@gmail.com");

        // when
        MemberDto.Info member = memberService.signUp(createMemberDto);

        // then
        Assertions.assertEquals("", member.getDescription());
        Assertions.assertEquals(createMemberDto.getUsername(), member.getUsername());
        Assertions.assertTrue(passwordEncoder.matches(createMemberDto.getPassword(), member.getPassword()));
        Assertions.assertEquals(createMemberDto.getEmail(), member.getEmail());

        Assertions.assertEquals(createMemberDto.getUsername(), member.getNickname());

    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void checkLogin() {

    }

    @Test
    @DisplayName("특정 유저 찾기 성공 테스트")
    void checkFindMember() {

    }

    @Test
    @DisplayName("유저 리스트 출력하기 성공 테스트")
    void checkFindMembers() {

    }
}