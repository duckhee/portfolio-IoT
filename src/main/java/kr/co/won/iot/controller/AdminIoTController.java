package kr.co.won.iot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/admin/iot")
@RequiredArgsConstructor
public class AdminIoTController {

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

}
