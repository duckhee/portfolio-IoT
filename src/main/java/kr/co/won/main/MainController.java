package kr.co.won.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class MainController {

    @GetMapping
    public String mainPage() {
        return "index";
    }

    @GetMapping(path = "/login")
    public String LoginPage() {
        if (isLogin()) {
            return "redirect:/";
        }
        return "loginPage";
    }

    /**
     * check login user
     */
    private boolean isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            log.info("not login user :::: ");
            return false;
        }
        log.info("login user ::::");
        return true;
    }
}
