package miniProject.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.constant.SessionConst;
import miniProject.board.controller.validator.MemberAddDtoValidator;
import miniProject.board.dto.MemberDto;
import miniProject.board.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberAddDtoValidator memberAddDtoValidator;

    @InitBinder("memberAddDto")
    public void initMemberAddDtoBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(memberAddDtoValidator);
    }

    @GetMapping("/signUp")
    public String signUpForm(Model model) {
        model.addAttribute("memberAddDto", new MemberDto.Create());

        return "member/signUpForm";
    }

    @PostMapping("/signUp")
    public String signUp(@Validated @ModelAttribute MemberDto.Create createMemberDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        log.info("회원 가입 성공");

        if (bindingResult.hasErrors()) {
            return "member/signUpForm";
        }

        memberService.signUp(createMemberDto);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("memberLoginDto", new MemberDto.Login());

        return "member/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute MemberDto.Login memberLoginDto,
                        BindingResult bindingResult,
                        HttpServletRequest request,
                        @RequestParam(defaultValue = "/") String redirectURL) {

        if (bindingResult.hasErrors()) {
            return "member/loginForm";
        }

        MemberDto.Session loginMember = memberService.login(memberLoginDto);

        // 로그인 실패
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");

            return "member/loginForm";
        }

        log.debug("로그인에 성공하여 세션을 생성합니다.");

        // 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        // 만약 유저가 로그인 전용 페이지에 로그인 없이 접근한 경우 이전 요청 페이지로 리다이렉트
        // 아니면 홈페이지로 리다이렉트
        return "redirect:" + redirectURL;
    }
}