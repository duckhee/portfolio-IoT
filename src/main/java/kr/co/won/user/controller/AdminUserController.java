package kr.co.won.user.controller;

import kr.co.won.user.form.CreateMemberForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {


    @GetMapping(path = "/create")
    public String memberCreatePage(Model model) {
        model.addAttribute(new CreateMemberForm());
        return "admin/users/createMemberPage";
    }
}
