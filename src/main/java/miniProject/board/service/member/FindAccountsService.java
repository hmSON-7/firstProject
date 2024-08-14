package miniProject.board.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.auth.utils.RedisUtil;
import miniProject.board.entity.Member;
import miniProject.board.repository.MemberRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindAccountsService {

    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    // 1. 인증키 전송 서비스
    public String sendAuthKey(String email) {
        log.debug("Auth Service Entry");
        try {
            Random random = new Random();
            String code =  String.format("%06d", random.nextInt(999999)); // 6자리로 포맷

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Board: Your Authentication Key for Your Request to Find Your Accounts!");
            message.setText(email + " 님에게 보내는 인증 코드: " + code
                    + "\n이 인증 코드를 입력하시면 본인 인증이 완료됩니다."
                    + "\n타인에게 인증 코드를 양도하지 마십시오.");
            log.debug("to: " + email + ", key: " + code);
            mailSender.send(message);
            log.debug("전송 성공, 코드 만료 시간 생성 테스트");
            redisUtil.setDataExpire(code, email, 60*5L); // 코드 유효시간 설정
            log.debug("Success Sending Auth Email");

            return code;
        } catch(Exception e) {
            log.error("인증 코드를 전송하는 과정에서 오류가 발생하였습니다!");
        }

        return null;
    }

    // 2. 인증키 확인 서비스
    public String checkAuthNum(String email, String sentCode, String authCode) {
        String storedEmail = redisUtil.getData(authCode);

        if(!Objects.equals(sentCode, authCode)) {
            return "mismatch";
        } if(storedEmail == null) {
            return "expired";
        } if(!Objects.equals(email, storedEmail)) {
            return "mismatch";
        } return "approve";
    }

    // 3. 계정 목록 확인 서비스
    public List<String> showAccList(String email) {
        List<Member> memberList = memberRepository.findAllByEmail(email);
        if(memberList == null) {
            return null;
        }

        return memberList.stream()
                .map(Member::getUsername)
                .toList();
    }
}
