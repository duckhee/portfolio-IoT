package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/blogs/resources/upload")
@RequiredArgsConstructor
public class BlogResourceController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    private final BlogService blogService;

    @GetMapping
    public ResponseEntity fileUpload() {
        return null;
    }

    @PostMapping
    public ResponseEntity fileUploadResource() {
        return null;
    }
}
