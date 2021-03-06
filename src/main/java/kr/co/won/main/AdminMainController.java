package kr.co.won.main;

import kr.co.won.auth.LoginUser;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class AdminMainController {

    @GetMapping
    public String adminMainPage() {
        return "admin/index";
    }

    @GetMapping(path = "/login")
    public String adminLoginPage() {
        UserDomain loginUser = getLoginUser();
        if (loginUser == null) {
            return "admin/loginPage";
        }

        if (loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER)) {
            return "redirect:/admin";

        }
        return "redirect:/";
    }

    /**
     * get user information
     */
    private UserDomain getLoginUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        // login user
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        return principal.getUser();
    }


    /**
     * check login user and user roles
     */
    private boolean isAuth() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        // not login user
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        // user role not have ADMIN or MANAGER
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        UserDomain loginUser = principal.getUser();
        // user role not have
        if (!loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER)) {
            return false;
        }
        return true;
    }
}
