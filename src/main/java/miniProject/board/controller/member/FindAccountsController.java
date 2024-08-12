package miniProject.board.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.MemberDto;
import miniProject.board.service.member.FindAccountsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/findAccounts")
public class FindAccountsController {

    private final FindAccountsService findAccountsService;

    // 1. 이메일 인증 폼으로 이동
    @GetMapping("/emailCheck")
    public String emailForm(Model model) {
        model.addAttribute("emailRequestDto", new MemberDto.EmailRequest.forUsername());
        return "email/emailFormForAccounts";
    }

    // 2. 이메일 전송
    @PostMapping("/emailCheck")
    public String sendEmail(@Validated @ModelAttribute("emailRequestDto") MemberDto.EmailRequest.forUsername emailRequest,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Entry: Send Email Controller");

        if (bindingResult.hasErrors()) {
            return "redirect:/findAccounts/emailCheck";
        }

        String code = findAccountsService.sendAuthKey(emailRequest.getEmail());
        log.debug("to: " + emailRequest.getEmail() + ", code: " + code);

        // 인증 폼에 필요한 데이터를 리다이렉트 속성에 추가
        redirectAttributes.addFlashAttribute("email", emailRequest.getEmail());

        return "redirect:/findAccounts/emailAuth";
    }

    // 3. 이메일 인증 번호 입력 폼으로 이동
    @GetMapping("/emailAuth")
    public String emailAuthForm(@ModelAttribute("email") String email, Model model) {
        log.debug("Entry: email Auth Form to Find Accounts Controller");

        model.addAttribute("emailCheckDto", new MemberDto.EmailCheck.forUsername());
        model.addAttribute("email", email);

        return "email/AuthFormForAccounts";
    }

    // 4. 이메일 인증 번호 입력 확인
    @PostMapping("/emailAuth")
    public String checkKey(@ModelAttribute("email") String email,
                           @Validated @ModelAttribute("emailCheckDto") MemberDto.EmailCheck.forUsername emailCheck,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Entry: Check Auth Key Controller");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/findAccounts/emailAuth";
        }

        boolean check = findAccountsService.checkAuthNum(email, emailCheck.getAuthCode());

        if (!check) {
            log.debug("인증 번호 오류");
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/findAccounts/emailAuth";
        }

        log.debug("회원 인증 성공!");
        redirectAttributes.addFlashAttribute("email", email);
        return "redirect:/findAccounts/showAccounts";
    }

    // 5. 아이디 목록 출력 폼으로 이동
    @GetMapping("/showAccounts")
    public String showAccountsForm(@ModelAttribute("email") String email, Model model) {
        log.debug("Entry: Accounts List Controller");

        List<String> accounts = findAccountsService.showAccList(email);
        if (accounts == null) {
            log.debug("대상 계정 없음");
        }

        model.addAttribute("email", email);
        model.addAttribute("accounts", accounts);
        return "member/accounts";
    }
}
