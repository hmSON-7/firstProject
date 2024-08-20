package miniProject.board.controller.member;

import jakarta.servlet.http.HttpSession;
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
                            BindingResult bindingResult, HttpSession session) {
        log.debug("Entry: Send Email Controller");

        if (bindingResult.hasErrors()) {
            return "redirect:/findAccounts/emailCheck";
        }
        String code = findAccountsService.sendAuthKey(emailRequest.getEmail());
        log.debug("to: " + emailRequest.getEmail() + ", code: " + code);

        // 세션에 이메일과 인증 코드를 저장
        session.setAttribute("email", emailRequest.getEmail());
        session.setAttribute("sentCode", code);

        return "redirect:/findAccounts/emailAuth";
    }

    // 3. 이메일 인증 번호 입력 폼으로 이동
    @GetMapping("/emailAuth")
    public String emailAuthForm(HttpSession session, Model model) {
        log.debug("Entry: email Auth Form to Find Accounts Controller");

        // 세션에서 이메일과 인증 코드를 가져옴
        String email = (String) session.getAttribute("email");
        String sentCode = (String) session.getAttribute("sentCode");

        if (email == null || sentCode == null) {
            // 세션이 만료되거나 유효하지 않은 경우 다시 이메일 확인 페이지로
            return "redirect:/findAccounts/emailCheck";
        }

        model.addAttribute("emailAuthDto", new EmailDto.Auth());

        return "email/AuthFormForAccounts";
    }

    // 4. 이메일 인증 번호 입력 확인
    @PostMapping("/emailAuth")
    public String checkKey(@Validated @ModelAttribute("emailAUthDto") EmailDto.Auth emailCheck,
                           BindingResult bindingResult, HttpSession session, Model model,
                           RedirectAttributes redirectAttributes) {
        log.debug("Entry: Check Auth Key Controller");

        if (bindingResult.hasErrors()) {
            log.debug("Validation errors: {}", bindingResult.getAllErrors());
            return "redirect:/findAccounts/emailAuth";
        }

        // 세션에서 이메일과 인증 코드를 가져옴
        String email = (String) session.getAttribute("email");
        String sentCode = (String) session.getAttribute("sentCode");

        log.debug("인증 요청자 - email: " + email + ", code: " + sentCode);

        if (email == null || sentCode == null) {
            // 세션이 만료되거나 유효하지 않은 경우 다시 이메일 확인 페이지로
            return "redirect:/findAccounts/emailCheck";
        }

        String result = findAccountsService.checkAuthNum(email, sentCode, emailCheck.getCode());
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
            return "email/AuthFormForAccounts";
        }

        log.debug(result + " - 회원 인증 성공!");
        return "redirect:/findAccounts/showAccounts";
    }

    // 5. 아이디 목록 출력 폼으로 이동
    @GetMapping("/showAccounts")
    public String showAccountsForm(HttpSession session, Model model) {
        log.debug("Entry: Accounts List Controller");

        // 세션에서 이메일을 가져옴
        String email = (String) session.getAttribute("email");

        if (email == null) {
            // 세션이 만료되었거나 유효하지 않은 경우 다시 이메일 확인 페이지로
            return "redirect:/findAccounts/emailCheck";
        }

        List<String> accounts = findAccountsService.showAccList(email);
        if (accounts == null) {
            log.debug("대상 계정 없음");
        }

        model.addAttribute("accounts", accounts);
        return "member/accounts";
    }
}