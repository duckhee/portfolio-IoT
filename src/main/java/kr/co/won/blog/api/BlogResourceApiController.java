package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlogResourceApiController {
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;


}
