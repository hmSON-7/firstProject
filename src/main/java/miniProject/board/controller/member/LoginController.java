package miniProject.board.controller.member;

import miniProject.board.dto.MemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("memberLoginDto", new MemberDto.Login());

        return "member/loginForm";
    }

    @ResponseBody
    @GetMapping("/admin/login")
    public String adminLoginForm(Model model) {
        return "admin Login Page";
    }
}
