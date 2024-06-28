package miniProject.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.controller.constant.SessionConst;
import miniProject.board.dto.AdminLoginDto;
import miniProject.board.dto.AdminSessionDto;
import miniProject.board.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

//    @GetMapping("/admin/login")
//    public String loginForm (Model model){
//        model.addAttribute("adminLoginDto", new AdminLoginDto());
//        return "admin/login";
//    }

    @PostMapping("/admin/login")
    public String login(@Validated @ModelAttribute AdminLoginDto adminLoginDto,
                        BindingResult bindingResult,
                        HttpServletRequest request,
                        @RequestParam(defaultValue = "/") String redirectURL) {

        if (bindingResult.hasErrors()) {
            return "admin/login";
        }

        AdminSessionDto adminLogin = adminService.login(adminLoginDto);

        // 로그인 실패
        if (adminLogin == null) {
            bindingResult.reject("adminloginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "admin/login";
        }

        // 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_ADMIN, adminLogin);

        return "redirect:/admin/main";
    }

    @GetMapping("/admin/main")
    public String adminMain(@Login AdminSessionDto adminSessionDto, Model model){
        if (adminSessionDto == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("adminSessionDto", adminSessionDto);

        return "admin/main";
    }
}