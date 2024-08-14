package miniProject.board.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.EmailDto;
import miniProject.board.service.member.FindAccountsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        model.addAttribute("emailRequestDto", new EmailDto.RequestForUsername());
        return "email/emailFormForAccounts";
    }

    // 2. 이메일 전송
    @PostMapping("/emailCheck")
    public String sendEmail(@Validated @ModelAttribute("emailRequestDto") EmailDto.RequestForUsername emailRequest,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Entry: Send Email Controller");

        if (bindingResult.hasErrors()) {
            return "redirect:/findAccounts/emailCheck";
        }

        String code = findAccountsService.sendAuthKey(emailRequest.getEmail());
        log.debug("to: " + emailRequest.getEmail() + ", code: " + code);

        // 인증 폼에 필요한 데이터를 리다이렉트 속성에 추가
        redirectAttributes.addFlashAttribute("email", emailRequest.getEmail());
        redirectAttributes.addFlashAttribute("sentCode", code);

        return "redirect:/findAccounts/emailAuth";
    }

    // 3. 이메일 인증 번호 입력 폼으로 이동
    @GetMapping("/emailAuth")
    public String emailAuthForm(@ModelAttribute("email") String email,
                                @ModelAttribute("sentCode") String sentCode,
                                Model model) {
        log.debug("Entry: email Auth Form to Find Accounts Controller");

        model.addAttribute("emailCheckDto", new EmailDto.CheckForUsername());
        model.addAttribute("email", email);
        model.addAttribute("code", sentCode);

        return "email/AuthFormForAccounts";
    }

    // 4. 이메일 인증 번호 입력 확인
    @PostMapping("/emailAuth")
    public String checkKey(@Validated @ModelAttribute("emailCheckDto") EmailDto.CheckForUsername emailCheck,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.debug("Entry: Check Auth Key Controller");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("email", emailCheck.getEmail());
            redirectAttributes.addFlashAttribute("sentCode", emailCheck.getSentCode());
            return "redirect:/findAccounts/emailAuth";
        }

        String result = findAccountsService.checkAuthNum(
                emailCheck.getEmail(),
                emailCheck.getSentCode(),
                emailCheck.getAuthCode()
        );
        log.debug("결과 : " + result);

        if (result.equals("expired")) {
            // 인증 코드 만료 처리
            log.debug("인증 코드 만료됨");
            redirectAttributes.addFlashAttribute("error", "인증 코드가 만료되었습니다. 이메일을 다시 입력해 주세요.");
            return "redirect:/findAccounts/emailCheck";
        } else if(result.equals("mismatch")) {
            // 인증 코드 불일치 처리
            log.debug("인증 번호 오류");
            model.addAttribute("error", "잘못된 인증 코드입니다. 다시 시도해 주세요.");
            model.addAttribute("email", emailCheck.getEmail());
            model.addAttribute("sentCode", emailCheck.getSentCode());
            return "email/AuthFormForAccounts";
        }

        log.debug(result + " - 회원 인증 성공!");
        redirectAttributes.addFlashAttribute("email", emailCheck.getEmail());
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
