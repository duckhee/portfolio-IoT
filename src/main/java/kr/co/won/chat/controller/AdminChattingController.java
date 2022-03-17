package kr.co.won.chat.controller;

import kr.co.won.auth.AuthUser;
import kr.co.won.chat.domain.ChattingRoomDomain;
import kr.co.won.chat.form.CreateChattingRoomForm;
import kr.co.won.chat.service.ChattingService;
import kr.co.won.chat.validation.CreateRoomValidator;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.UpdateProvider;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(path = "/room")
    public String chattingRoomListPage(@AuthUser UserDomain loginUser, PageDto pageDto, Model model) {

        return "admin/chat/listChattingPage";
    }

    @GetMapping(path = "/room/create")
    public String chattingRoomCreatePage(@AuthUser UserDomain loginUser, Model model) {
        model.addAttribute(new CreateChattingRoomForm());
        model.addAttribute("user", loginUser);
        return "admin/chat/createChattingPage";
    }

    @PostMapping(path = "/room/create")
    public String chattingRoomCreateDo(@AuthUser UserDomain loginUser, @Validated CreateChattingRoomForm form, Errors errors, Model model, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            model.addAttribute("user", loginUser);
            return "admin/chat/createChattingPage";
        }
        flash.addFlashAttribute("msg", form.getName() + " create chatting room.");
        return "redirect:/admin/chatting/room";
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

    @GetMapping(path = "/room/{roomId}/update")
    public String chattingRoomUpdatePage(@AuthUser UserDomain loginUser, @PathVariable(name = "roomId") String id, Model model) {
        ChattingRoomDomain findChatRoom = chattingService.findChatRoom(id, loginUser);
        model.addAttribute("room", findChatRoom);
        return "admin/chat/updateChattingPage";
    }

    @PostMapping(path = "/room/{roomId}/update")
    public String chattingRoomUpdateDo(@AuthUser UserDomain loginUser, @PathVariable(name = "roomId") String id, Model model) {
        return "redirect:/admin/chatting/room";
    }

    @PutMapping(path = "/room/{roomId}")
    public ResponseEntity updateChattingRoomResource(@AuthUser UserDomain loginUser, @PathVariable(name = "roomId") String roomId) {
        return ResponseEntity.ok().body("");
    }
}
