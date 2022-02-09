package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.service.BlogService;
import kr.co.won.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
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
    public ResponseEntity fileUploadResource(@AuthUser UserDomain loginUser, @RequestParam MultipartFile upload) {
        log.info("login user ::: {}", loginUser);
        String originalFilename = upload.getOriginalFilename();
        log.info("upload file name ::: {}", originalFilename);
        return null;
    }
}
