package miniProject.board.service;

import miniProject.board.dto.MemberAddDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.dto.MemberLoginDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceImplTest {
    @Autowired
    MemberServiceImpl memberService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    MemberAddDto memberAddDto = null;
    @BeforeEach
    void init() {
        memberAddDto = new MemberAddDto(
                "admin",
                "123456",
                "123456",
                "admin1@gmail.com"
        );
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void checkSignUp() {
        // when
        Long saveId = memberService.signUp(memberAddDto).getId();

        // then
        MemberDto memberDto = memberService.findMember(saveId);
        // userId 비교
        assertThat(memberDto).extracting(MemberDto::getUserId).isEqualTo(memberAddDto.getUserId());
        // 암호화된 비밀번호 비교
        assertThat(passwordEncoder.matches(memberAddDto.getPassword(), memberDto.getPassword())).isTrue();
        // password, confirmPassword 비교
        assertThat(memberAddDto.getPassword()).isEqualTo(memberAddDto.getConfirmPassword());
        // Email 비교
        assertThat(memberDto).extracting(MemberDto::getEmail).isEqualTo(memberAddDto.getEmail());
        // 설명을 공란으로 생성했는지 확인
        assertThat(memberDto).extracting(MemberDto::getDescription).isEqualTo("");
        // 초기 닉네임이 아이디와 동일하게 설정되었는지 비교
        assertThat(memberDto).extracting(MemberDto::getNickname).isEqualTo(memberDto.getUserId());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void checkLogin() {
        // given
        memberService.signUp(memberAddDto);

        MemberLoginDto memberLoginDto = new MemberLoginDto(
                "admin",
                "123456"
        );

        // when
        Long saveId = memberService.login(memberLoginDto).getId();

        // then
        MemberDto memberDto = memberService.findMember(saveId);
        // userId 비교
        assertThat(memberDto).extracting(MemberDto::getUserId).isEqualTo(memberLoginDto.getUserId());
        // 암호화된 비밀번호 비교
        assertThat(passwordEncoder.matches(memberLoginDto.getPassword(), memberDto.getPassword())).isTrue();
    }

    @Test
    @DisplayName("특정 유저 찾기 성공 테스트")
    void checkFindMember() {
        // given
        Long findId = memberService.signUp(memberAddDto).getId();

        // when
        MemberDto memberDto = memberService.findMember(findId);

        // then
        assertThat(memberDto).extracting(MemberDto::getId).isEqualTo(findId);
    }

    @Test
    @DisplayName("유저 리스트 출력하기 성공 테스트")
    void checkFindMembers() {
        // given
        memberService.signUp(memberAddDto);

        MemberAddDto memberAddDto2 = new MemberAddDto(
                "wjsansrk",
                "123456789",
                "123456789",
                "wjsansrk12@gmail.com"
        );
        memberService.signUp(memberAddDto2);

        // when
        List<MemberDto> memberList = memberService.findMembers();

        // then
        // 멤버 리스트가 null이 아닌지 확인
        assertThat(memberList).isNotNull();
        // 멤버 리스트의 크기가 2인지 확인
        assertThat(memberList).hasSize(2);

        // 각 멤버의 속성을 하나씩 비교
        assertThat(memberList.get(0)).extracting(MemberDto::getUserId).isEqualTo(memberAddDto.getUserId());
        assertThat(memberList.get(1)).extracting(MemberDto::getUserId).isEqualTo(memberAddDto2.getUserId());
    }
}