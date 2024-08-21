package miniProject.board.controller.member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.EmailDto;
import miniProject.board.service.member.PwChangeService;
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
    private final PwChangeService pwChangeService;
    // 1. 이메일 인증 폼으로 이동
    @GetMapping("/emailCheck")
    public String emailForm(Model model) {
        model.addAttribute("emailRequestDto", new EmailDto.RequestForPW());
        return "email/emailFormForPw";
    }
    // 2. 이메일 등록 여부 확인
    @PostMapping("/emailCheck")
    public String sendEmail(@Validated @ModelAttribute("emailRequestDto") EmailDto.RequestForPW emailRequest,
                            BindingResult bindingResult, HttpSession session, Model model) {
        log.debug("Entry: Send Email Controller");

        if (bindingResult.hasErrors()) {
            return "redirect:/changePW/emailCheck";
        }
        String code = pwChangeService.checkEmail(emailRequest.getUsername(), emailRequest.getEmail());
        if(code == null) {
            log.debug("ID가 없거나 등록되지 않은 이메일입니다.");
            model.addAttribute("error", "ID가 없거나 등록되지 않은 이메일입니다. 다시 입력해주세요.");
            return "email/emailFormForPw";
        }

        log.debug("to: " + emailRequest.getEmail() + ", code: " + code);

        // 세션에 이메일, ID, 인증 코드를 저장
        session.setAttribute("username", emailRequest.getUsername());
        session.setAttribute("email", emailRequest.getEmail());
        session.setAttribute("sentCode", code);

        // 리다이렉트로 인증 폼으로 이동
        return "redirect:/changePW/emailAuth";
    }

    // 3. 이메일 인증 번호 입력 폼으로 이동
    @GetMapping("/emailAuth")
    public String emailAuthForm(HttpSession session, Model model) {
        log.debug("Entry: email Auth Form to Change Password Controller");

        // 세션에서 이메일, ID, 인증 코드를 가져옴
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");
        String sentCode = (String) session.getAttribute("sentCode");

        if (username == null || email == null || sentCode == null) {
            // 세션이 만료되거나 유효하지 않은 경우 다시 이메일 확인 페이지로
            return "redirect:/changePW/emailCheck";
        }

        model.addAttribute("emailCheckDto", new EmailDto.Auth());

        return "email/AuthFormForPw";
    }

    // 4. 이메일 인증 번호 입력 확인
    @PostMapping("/emailAuth")
    public String checkKey(@Validated @ModelAttribute("emailCheckDto") EmailDto.Auth emailCheck,
                           BindingResult bindingResult, HttpSession session, Model model,
                           RedirectAttributes redirectAttributes) {
        log.debug("Entry: Check Auth Key Controller");

        if (bindingResult.hasErrors()) {
            log.debug("Validation errors: {}", bindingResult.getAllErrors());
            return "redirect:/changePW/emailAuth";
        }

        // 세션에서 이메일, ID, 인증 코드를 가져옴
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");
        String sentCode = (String) session.getAttribute("sentCode");

        log.debug("인증 요청자 - username: " + username + ", email: " + email + ", code: " + sentCode);

        if (username == null ||email == null || sentCode == null) {
            // 세션이 만료되거나 유효하지 않은 경우 다시 이메일 확인 페이지로
            return "redirect:/changePW/emailCheck";
        }

        String result = pwChangeService.checkAuthNum(email, sentCode, emailCheck.getCode());
        log.debug("결과 : " + result);

        if (result.equals("expired")) {
            // 인증 코드 만료 처리
            log.debug("인증 코드 만료됨");
            redirectAttributes.addFlashAttribute("error", "인증 코드가 만료되었습니다. 이메일을 다시 입력해 주세요.");
            return "redirect:/changePW/emailCheck";
        } else if(result.equals("mismatch")) {
            // 인증 코드 불일치 처리
            log.debug("인증 번호 오류");
            model.addAttribute("error", "잘못된 인증 코드입니다. 다시 시도해 주세요.");
            return "email/AuthFormForPw";
        }

        log.debug("회원 인증 성공!");
        return "redirect:/changePW/newPassword";
    }

    // 5. 신규 비밀번호 입력 폼으로 이동
    @GetMapping("/newPassword")
    public String changePWForm(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            // 세션이 만료되었거나 유효하지 않은 경우 다시 이메일 확인 페이지로
            return "redirect:/changePW/emailCheck";
        }

        model.addAttribute("changePWDto", new EmailDto.ChangePW());
        return "member/changePwForm";
    }

    // 6. 신규 비밀번호 저장
    @PatchMapping("/newPassword")
    public String changePW(@Validated @ModelAttribute("changePWDto") EmailDto.ChangePW changePWDto,
                           BindingResult bindingResult, HttpSession session, Model model) {
        log.debug("Entry: Change Password Controller");

        if (bindingResult.hasErrors()) {
            log.debug("Validation errors: {}", bindingResult.getAllErrors());
            return "redirect:/changePW/newPassword";
        }

        String username = (String) session.getAttribute("username");
        if (username == null) {
            // 세션이 만료되었거나 유효하지 않은 경우 다시 이메일 확인 페이지로
            return "redirect:/changePW/emailCheck";
        }

        String result = pwChangeService.changePW(
                username, changePWDto.getPassword(), changePWDto.getConfirmPassword()
        );
        if (result.equals("mismatch")) {
            log.debug("비밀번호, 비밀번호 확인 불일치");
            model.addAttribute("error", "비밀번호와 비밀번호 확인이 일치하지 않습니다. 다시 입력해주세요.");
            return "member/changePwForm";
        } else if(result.equals("unidentified")) {
            log.debug("확인할 수 없는 사용자");
            return "redirect:/changePW/emailCheck";
        }
        log.debug("비밀번호 변경 성공");
        return "redirect:/login";
    }
}