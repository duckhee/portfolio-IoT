package kr.co.won.iot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageAssembler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(path = "/api/iot")
@RequiredArgsConstructor
public class IoTApiController {

    /**
     * Null Value Skip Model Mapper
     */
    @Resource(name = "skipModelMapper")
    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    /**
     * Paging Assembler
     */
    private final PageAssembler pageAssembler;

    @GetMapping
    public ResponseEntity IoTListResource(@AuthUser UserDomain loginUser) {
        return null;
    }

}
