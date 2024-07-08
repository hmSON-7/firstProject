package miniProject.board.service;

import miniProject.board.dto.MemberDto;
import miniProject.board.service.member.MemberServiceImpl;
import miniProject.board.service.member.SignUpService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@Transactional
class MemberServiceImplTest {
    @Autowired
    MemberServiceImpl memberService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    SignUpService signUpService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void checkSignUp() {
        // given
        MemberDto.Create createMemberDto = new MemberDto.Create();
        createMemberDto.setUsername("test");
        createMemberDto.setPassword("123456");
        createMemberDto.setConfirmPassword("123456");
        createMemberDto.setEmail("test@gmail.com");

        // when
        MemberDto.Info member = signUpService.signUp(createMemberDto);

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
        // given
        MemberDto.Create createMemberDto = new MemberDto.Create();
        createMemberDto.setUsername("test");
        createMemberDto.setPassword("123456");
        createMemberDto.setConfirmPassword("123456");
        createMemberDto.setEmail("test@gmail.com");
        MemberDto.Info memberInfo = signUpService.signUp(createMemberDto);

        // when
        MemberDto.Info findMember = memberService.findMember(memberInfo.getId());

        // then
        Assertions.assertEquals(memberInfo.getId(), findMember.getId());
        Assertions.assertTrue(passwordEncoder.matches(createMemberDto.getPassword(), findMember.getPassword()));

        Assertions.assertEquals(memberInfo.getUsername(), findMember.getUsername());
        Assertions.assertEquals(memberInfo.getNickname(), findMember.getNickname());

        Assertions.assertEquals(memberInfo.getEmail(), findMember.getEmail());
    }

    @Test
    @DisplayName("회원 리스트 조회 성공 테스트")
    void checkFindMembers() {
        // given
        MemberDto.Create createMemberDto1 = new MemberDto.Create();
        createMemberDto1.setUsername("test1");
        createMemberDto1.setPassword("123456");
        createMemberDto1.setConfirmPassword("123456");
        createMemberDto1.setEmail("test@gmail.com");

        MemberDto.Create createMemberDto2 = new MemberDto.Create();
        createMemberDto2.setUsername("test2");
        createMemberDto2.setPassword("123456");
        createMemberDto2.setConfirmPassword("123456");
        createMemberDto2.setEmail("test@gmail.com");

        signUpService.signUp(createMemberDto1);
        signUpService.signUp(createMemberDto2);

        // when
        List<MemberDto.Info> members = memberService.findMembers();

        // then
        Assertions.assertNotNull(members);
        Assertions.assertEquals(3, members.size());
    }
}