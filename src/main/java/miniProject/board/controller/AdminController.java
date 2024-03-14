package miniProject.board.controller;

import miniProject.board.entity.Admin;
import miniProject.board.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @Autowired

    private AdminRepository adminRepository;
    @GetMapping("/admin/login")
    public String adminLogin (){
        return "admin/login";
    }
    @GetMapping("admin/main")
    public String adminMain(){
        return "admin/main";
    }
}
