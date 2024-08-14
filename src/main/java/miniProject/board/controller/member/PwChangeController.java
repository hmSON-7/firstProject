package miniProject.board.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.EmailDto;
import miniProject.board.dto.MemberDto;
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
                            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.debug("Entry: Send Email Controller");

        if (bindingResult.hasErrors()) {
            return "redirect:/changePW/emailCheck";
        }

        String code = pwChangeService.checkEmail(emailRequest.getUsername(), emailRequest.getEmail());
        if(code == null) {
            log.debug("ID가 없거나 등록되지 않은 이메일입니다.");
            model.addAttribute("error", "ID가 없거나 등록되지 않은 이메일입니다. 다시 입력해주세요.");
            model.addAttribute("email", emailRequest.getEmail());
            model.addAttribute("username", emailRequest.getUsername());
            return "email/emailFormForPw";
        }

        log.debug("to: " + emailRequest.getEmail() + ", code: " + code);

        // 인증 폼에 필요한 데이터를 리다이렉트 속성에 추가
        redirectAttributes.addFlashAttribute("username", emailRequest.getUsername());
        redirectAttributes.addFlashAttribute("email", emailRequest.getEmail());
        redirectAttributes.addFlashAttribute("sentCode", code);

        // 리다이렉트로 인증 폼으로 이동
        return "redirect:/changePW/emailAuth";
    }

    // 3. 이메일 인증 번호 입력 폼으로 이동
    @GetMapping("/emailAuth")
    public String emailAuthForm(@ModelAttribute("username") String username,
                                @ModelAttribute("email") String email,
                                @ModelAttribute("sentCode") String sentCode,
                                Model model) {
        log.debug("Entry: email Auth Form to Change Password Controller");

        model.addAttribute("emailCheckDto", new EmailDto.CheckForPW());
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        model.addAttribute("code", sentCode);

        return "email/AuthFormForPw";
    }

    // 4. 이메일 인증 번호 입력 확인
    @PostMapping("/emailAuth")
    public String checkKey(@Validated @ModelAttribute("emailCheckDto") EmailDto.CheckForPW emailCheck,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.debug("Entry: Check Auth Key Controller");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("username", emailCheck.getUsername());
            redirectAttributes.addFlashAttribute("email", emailCheck.getEmail());
            redirectAttributes.addFlashAttribute("sentCode", emailCheck.getSentCode());
            return "redirect:/changePW/emailAuth";
        }

        String result = pwChangeService.checkAuthNum(
                emailCheck.getEmail(),
                emailCheck.getSentCode(),
                emailCheck.getAuthCode()
        );
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
            model.addAttribute("username", emailCheck.getUsername());
            model.addAttribute("email", emailCheck.getEmail());
            model.addAttribute("sentCode", emailCheck.getSentCode());
            return "email/AuthFormForPw";
        }

        log.debug("회원 인증 성공!");
        redirectAttributes.addFlashAttribute("username", emailCheck.getUsername());
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
                           BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.debug("Entry: Change Password Controller");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("username", changePWDto.getUsername());
            return "redirect:/changePW/newPassword";
        }

        String result = pwChangeService.changePW(
                changePWDto.getUsername(), changePWDto.getPassword(), changePWDto.getConfirmPassword()
        );
        if (result.equals("mismatch")) {
            log.debug("비밀번호, 비밀번호 확인 불일치");
            model.addAttribute("username", changePWDto.getUsername());
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
