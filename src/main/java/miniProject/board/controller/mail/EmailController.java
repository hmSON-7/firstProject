package miniProject.board.controller.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.service.mail.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/emailForm")
    public String showEmailForm(Model model) {
        return "email/emailForm";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam(name="to") String to,
                            @RequestParam(name="subject") String subject,
                            @RequestParam(name="text") String text, Model model) {
        log.debug("Entry");
        emailService.sendSimpleMessage(to, subject, text);
        log.debug("text: " + text);
        model.addAttribute("message", "Email sent successfully");
        return "email/emailResult";
    }
}