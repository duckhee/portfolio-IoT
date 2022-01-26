package kr.co.won.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class AdminMainController {

    @GetMapping(path = "/login")
    public String adminLoginPage() {
        return "admin/loginPage";
    }
}
