package kr.co.won.chat.controller;

import kr.co.won.auth.AuthUser;
import kr.co.won.chat.service.ChattingService;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "/chat")
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;

    @GetMapping
    public String chattingRoomListPage(@AuthUser UserDomain loginUser, PageDto pageDto, Model model) {
        return "";
    }

    @GetMapping(path = "/room/create")
    public String createChattingRoomPage(@AuthUser UserDomain loginUser, Model model){
        return "";
    }

    @PostMapping(path = "/room/create")
    public String createChattingRoomDo(@AuthUser UserDomain loginUser, Model model, RedirectAttributes flash){
        return "";
    }

}
