package miniProject.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.validator.MemberAddDtoValidator;
import miniProject.board.dto.MemberAddDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.dto.MemberLoginDto;
import miniProject.board.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberAddDtoValidator memberAddDtoValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(memberAddDtoValidator);
    }

    @GetMapping("/signUp")
    public String signUpForm(Model model) {
        model.addAttribute("memberAddDto", new MemberAddDto());

        return "member/signUpForm";
    }

    @PostMapping("/signUp")
    public String signUp(@Validated @ModelAttribute MemberAddDto memberAddDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "member/signUpForm";
        }

        memberService.signUp(memberAddDto);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("memberLoginDto", new MemberLoginDto());

        return "member/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute MemberLoginDto memberLoginDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "member/loginForm";
        }

        MemberDto loggedInMember = memberService.login(memberLoginDto);
        if (loggedInMember != null) {
            redirectAttributes.addFlashAttribute("successMessage", "로그인에 성공하였습니다.");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "redirect:/member/login";
        }
    }
}