package kr.co.won.chat.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatApiController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;


    @GetMapping
    public ResponseEntity chattingRoomListResource(@AuthUser UserDomain authUser, PageDto pageDto) {
        return null;
    }
}
