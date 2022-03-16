package kr.co.won.chat.controller;

import kr.co.won.auth.AuthUser;
import kr.co.won.chat.domain.ChattingRoomDomain;
import kr.co.won.chat.form.CreateChattingRoomForm;
import kr.co.won.chat.service.ChattingService;
import kr.co.won.chat.validation.CreateRoomValidator;
import kr.co.won.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

@Controller
@RequestMapping(path = "/admin/chatting")
@RequiredArgsConstructor
public class AdminChattingController {

    @Resource(name = "skipModelMapper")
    private final ModelMapper modelMapper;

    @Resource(name = "notSkipModelMapper")
    private final ModelMapper notSkipModelMapper;
    /**
     * chatting service
     */
    private final ChattingService chattingService;

    private final CreateRoomValidator createRoomValidator;

    @InitBinder(value = "createChattingRoomForm")
    public void createValidator(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(createRoomValidator);
    }


    @GetMapping(path = "/room/create")
    public String chattingRoomCreatePage(@AuthUser UserDomain loginUser, Model model) {
        model.addAttribute(new CreateChattingRoomForm());
        model.addAttribute("user", loginUser);
        return "";
    }

    /**
     * Create Chatting Room make Ajax
     */
    @PostMapping(path = "/room")
    public ResponseEntity chattingRoomCreateResource(@AuthUser UserDomain loginUser, @Validated CreateChattingRoomForm form, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        ChattingRoomDomain mappedRoom = modelMapper.map(form, ChattingRoomDomain.class);

        chattingService.createChatRoom(mappedRoom, loginUser);
        return ResponseEntity.ok().build();
    }

    /**
     * chatting create page call
     */
    @PostMapping(path = "/room/create")
    public String chattingRoomCreateDo(@AuthUser UserDomain loginUser, @Validated CreateChattingRoomForm form, Errors errors, Model model, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            model.addAttribute("user", loginUser);
            return "";
        }
        ChattingRoomDomain mappedRoom = modelMapper.map(form, ChattingRoomDomain.class);
        ChattingRoomDomain savedRoom = chattingService.createChatRoom(mappedRoom, loginUser);
        flash.addFlashAttribute("msg", savedRoom.getName() + " room created.");
        return "redirect:/admin/chatting/room";
    }
}
