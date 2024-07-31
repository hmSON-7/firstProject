package miniProject.board.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.constant.LogConst;
import miniProject.board.controller.validator.MemberAddDtoValidator;
import miniProject.board.dto.MemberDto;
import miniProject.board.service.member.SignUpService;
import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SignUpController {
    private final SignUpService signUpService;
    private final MemberAddDtoValidator memberAddDtoValidator;

    @InitBinder("createMemberDto")
    public void initMemberAddDtoBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(memberAddDtoValidator);
    }

    @GetMapping("/signUp")
    public String signUpForm(Model model) {
        model.addAttribute("createMemberDto", new MemberDto.Create());

        return "member/signUpForm";
    }

    @PostMapping("/signUp")
    public String signUp(@Validated @ModelAttribute("createMemberDto") MemberDto.Create createMemberDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "member/signUpForm";
        }

        signUpService.signUp(createMemberDto);

        log.info("[{}] 회원 가입 성공", MDC.get(LogConst.TRACE_ID));

        return "redirect:/login";
    }
}
