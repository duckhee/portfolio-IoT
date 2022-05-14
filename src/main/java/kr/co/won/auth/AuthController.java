package kr.co.won.auth;


import kr.co.won.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class AuthController {


    @GetMapping(path = "/auth/failed")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String oauthFailedPage(@AuthUser UserDomain loginUser) {
        if(loginUser != null){

        }
        return "";
    }

    @GetMapping(path = "/api/login-failed")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity loginFaileResourceResponse(@AuthUser UserDomain loginUser){
        if(loginUser != null){

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }
}
