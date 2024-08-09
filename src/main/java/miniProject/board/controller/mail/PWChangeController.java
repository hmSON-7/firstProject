package miniProject.board.controller.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.MemberDto;
import miniProject.board.service.mail.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/changePW")
public class PWChangeController {

    private final EmailService emailService;

    // 1. 이메일 인증 폼으로 이동
    @GetMapping("/emailCheck")
    public String emailForm(Model model) {
        model.addAttribute("emailRequestDto", new MemberDto.EmailRequest.forPW());
        return "email/emailForm";
    }

    // 2. 이메일 등록 여부 확인
    @PostMapping("/emailCheck")
    public String sendEmail(@Validated @ModelAttribute("emailRequestDto") MemberDto.EmailRequest.forPW emailRequest,
                            BindingResult bindingResult) {
        log.debug("Entry: Send Email Controller");

        if (bindingResult.hasErrors()) {
            return "redirect:/changePW/emailCheck";
        }

        String code = emailService.checkEmail(emailRequest.getUsername(), emailRequest.getEmail());
        log.debug("to: " + emailRequest.getEmail() + ", code: " + code);

        return "redirect:/changePW/emailAuth?username=" + emailRequest.getUsername();
    }

    // 3. 이메일 인증 번호 입력 폼으로 이동
    @GetMapping("/emailAuth")
    public String emailAuthForm(@RequestParam("username") String username, Model model) {
        log.debug("Entry: email Auth Form to Change Password Controller");
        model.addAttribute("emailCheckDto", new MemberDto.EmailCheck.forPW());
        model.addAttribute("username", username);  // 사용자 이름을 모델에 추가
        return "email/emailAuthForm";
    }

    // 4. 이메일 인증 번호 입력 확인
    @PostMapping("/emailAuth")
    public String checkKey(@RequestParam("username") String username,
                           @Validated @ModelAttribute("emailCheckDto") MemberDto.EmailCheck.forPW emailCheck,
                           BindingResult bindingResult) {
        log.debug("Entry: Check Auth Key Controller");

        if (bindingResult.hasErrors()) {
            return "redirect:/changePW/emailAuth?username=" + username;
        }

        boolean check = emailService.checkAuthNum(
                emailCheck.getEmail(),
                emailCheck.getAuthCode()
        );

        if (!check) {
            log.debug("인증 번호 오류");
            return "redirect:/changePW/emailAuth?username=" + username;
        }

        log.debug("회원 인증 성공!");
        return "redirect:/changePW/newPassword?username=" + username;
    }

    // 5. 신규 비밀번호 입력 폼으로 이동
    @GetMapping("/newPassword")
    public String changePWForm(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("password", "");
        return "member/changePWForm";
    }

    // 6. 신규 비밀번호 저장
    @PatchMapping("/newPassword")
    public String changePW(@RequestParam("username") String username, @Validated @ModelAttribute("newPW") String password,
                           BindingResult bindingResult) {
        log.debug("Entry: Change Password Controller");

        if (bindingResult.hasErrors()) {
            return "redirect:/changePW/newPassword?username=" + username;
        }

        boolean check = emailService.changePW(username, password);
        if (!check) {
            log.debug("비밀번호 변경 중 오류 발생");
            return "redirect:/changePW/newPassword?username=" + username;
        }

        log.debug("비밀번호 변경 성공");
        return "redirect:/login";
    }
}
