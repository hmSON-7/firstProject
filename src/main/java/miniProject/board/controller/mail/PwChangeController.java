package miniProject.board.controller.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.MemberDto;
import miniProject.board.service.mail.PwChangeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/changePW")
public class PwChangeController {

    private final PwChangeService PWChangeService;

    // 1. 이메일 인증 폼으로 이동
    @GetMapping("/emailCheck")
    public String emailForm(Model model) {
        model.addAttribute("emailRequestDto", new MemberDto.EmailRequest.forPW());
        return "email/emailFormForPw";
    }

    // 2. 이메일 등록 여부 확인
    @PostMapping("/emailCheck")
    public String sendEmail(@Validated @ModelAttribute("emailRequestDto") MemberDto.EmailRequest.forPW emailRequest,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Entry: Send Email Controller");

        if (bindingResult.hasErrors()) {
            return "redirect:/changePW/emailCheck";
        }

        String code = PWChangeService.checkEmail(emailRequest.getUsername(), emailRequest.getEmail());
        log.debug("to: " + emailRequest.getEmail() + ", code: " + code);

        // 인증 폼에 필요한 데이터를 리다이렉트 속성에 추가
        redirectAttributes.addFlashAttribute("username", emailRequest.getUsername());
        redirectAttributes.addFlashAttribute("email", emailRequest.getEmail());

        // 리다이렉트로 인증 폼으로 이동
        return "redirect:/changePW/emailAuth";
    }

    // 3. 이메일 인증 번호 입력 폼으로 이동
    @GetMapping("/emailAuth")
    public String emailAuthForm(@ModelAttribute("username") String username,
                                @ModelAttribute("email") String email, Model model) {
        log.debug("Entry: email Auth Form to Change Password Controller");

        model.addAttribute("emailCheckDto", new MemberDto.EmailCheck.forPW());
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        return "email/AuthFormForPw";
    }

    // 4. 이메일 인증 번호 입력 확인
    @PostMapping("/emailAuth")
    public String checkKey(@ModelAttribute("email") String email, @ModelAttribute("username") String username,
                           @Validated @ModelAttribute("emailCheckDto") MemberDto.EmailCheck.forPW emailCheck,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Entry: Check Auth Key Controller");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("username", username);
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/changePW/emailAuth";
        }

        boolean check = PWChangeService.checkAuthNum(email, emailCheck.getAuthCode());

        if (!check) {
            log.debug("인증 번호 오류");
            redirectAttributes.addFlashAttribute("username", username);
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/changePW/emailAuth";
        }

        log.debug("회원 인증 성공!");
        redirectAttributes.addFlashAttribute("username", username);
        return "redirect:/changePW/newPassword";
    }

    // 5. 신규 비밀번호 입력 폼으로 이동
    @GetMapping("/newPassword")
    public String changePWForm(@ModelAttribute("username") String username, Model model) {
        model.addAttribute("changePWDto", new MemberDto.ChangePW());
        model.addAttribute("username", username);
        return "member/changePwForm";
    }

    // 6. 신규 비밀번호 저장
    @PatchMapping("/newPassword")
    public String changePW(@Validated @ModelAttribute("changePWDto") MemberDto.ChangePW changePWDto,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Entry: Change Password Controller");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("username", changePWDto.getUsername());
            return "redirect:/changePW/newPassword";
        }

        boolean check = PWChangeService.changePW(changePWDto.getUsername(), changePWDto.getPassword());
        if (!check) {
            log.debug("비밀번호 변경 중 오류 발생");
            redirectAttributes.addFlashAttribute("username", changePWDto.getUsername());
            return "redirect:/changePW/newPassword";
        }

        log.debug("비밀번호 변경 성공");
        return "redirect:/login";
    }
}
