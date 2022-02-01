package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/admin/blogs")
@RequiredArgsConstructor
public class AdminBlogController {

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;



}
