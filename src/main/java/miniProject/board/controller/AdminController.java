package miniProject.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.MemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/admin/main")
    public String adminMain(@Login MemberDto.Session memberSessionDto, Model model){
        if (memberSessionDto == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("adminSessionDto", memberSessionDto);

        return "admin/main";
    }
}