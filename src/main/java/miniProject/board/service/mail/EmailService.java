package miniProject.board.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.entity.Member;
import miniProject.board.repository.MemberRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;

    // 1. 이메일 전송 테스트 코드
    public void sendSimpleMessage(String to, String subject, String text) {
        log.debug("Service Entry");
        Random random = new Random();
        String code =  String.format("%06d", random.nextInt(999999));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(code);
        log.debug("to: " + to + ", subject: " + subject);
        log.debug("text: " + code);
        mailSender.send(message);
        log.debug("Success Sending Email");
    }

    // 2. 인증키 전송 서비스
    public void sendAuthKey(String username, String email, String code) {
        log.debug("Auth Service Entry");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Authentication");
        message.setText(username + "님에게 보내는 인증키: " + code);
        log.debug("to: " + email + ", username: " + username + ", key: " + code);
        mailSender.send(message);
        log.debug("Success Sending Auth Email");
    }

    // 3. 이메일 등록 여부 확인 서비스
    public String checkEmail(String username, String email) {
        Member member = memberRepository.findByUsername(username).orElse(null);
        if(member == null) {
            log.error("error: " + username + " ID를 찾을 수 없습니다.");
            return null;
        }

        if(!Objects.equals(member.getEmail(), email)) {
            log.info(email + ": 등록된 이메일과 일치하지 않습니다.");
            return null;
        }

        Random random = new Random();
        String code =  String.format("%06d", random.nextInt(999999)); // 6자리로 포맷

        sendAuthKey(username, email, code);
        return code;
    }

    // 4. 인증키 확인 서비스(이거 한 줄인데 굳이 필요한가?)
    public boolean checkAuthKey(String code, String input) {
        return Objects.equals(code, input);
    }

    // 5. 비밀번호 변경 서비스(미완, 차후 MemberService로 위치 변경)
    public String changePW(String username, String password) {
        Member member = memberRepository.findByUsername(username).orElse(null);
        if(member == null) {
            log.error("error: " + username + " ID를 찾을 수 없습니다.");
            return null;
        }

        // Member 엔티티에 비밀번호 변경 메서드 추가
        // 입력된 새 비밀번호를 엔티티에 저장한 후 DB에 저장

        return null;
    }
}
