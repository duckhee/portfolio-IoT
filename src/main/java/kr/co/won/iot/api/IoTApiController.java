package kr.co.won.iot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(path = "/api/iot")
@RequiredArgsConstructor
public class IoTApiController {

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;


}
