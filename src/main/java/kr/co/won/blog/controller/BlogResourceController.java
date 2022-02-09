package kr.co.won.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.blog.domain.BlogResourceDomain;
import kr.co.won.blog.service.BlogService;
import kr.co.won.blog.util.BlogResourceFileUtil;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.file.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Slf4j
@Controller
@RequestMapping(path = "/blogs/resources/upload")
@RequiredArgsConstructor
public class BlogResourceController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    private final BlogService blogService;

    /**
     * blog upload file util
     */
    private final BlogResourceFileUtil fileUtil;

    @GetMapping
    public ResponseEntity fileUpload() {
        return null;
    }

    @PostMapping
    public ResponseEntity fileUploadResource(@AuthUser UserDomain loginUser, @RequestParam(name = "upload") MultipartFile uploadFile) {
        log.info("login user ::: {}", loginUser);
        String originalFilename = uploadFile.getOriginalFilename();
        long size = uploadFile.getSize();
        String name = uploadFile.getName();
        String contentType = uploadFile.getContentType();
        URI uri = URI.create("/uploads/test");
        log.info("upload file name ::: {}, size ::: {}, name :::: {}, content type :::: {}", originalFilename, size, name, contentType);
        try {
            BlogResourceDomain savedBlogResource = fileUtil.fileUpload(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // printWriter.println("{\"filename\" : \""+fileName+"\", \"uploaded\" : 1, \"url\":\""+fileUrl+"\"}");

        return ResponseEntity.ok().body("{\"filename\" : \""+originalFilename+"\", \"uploaded\" : 1, \"url\":\""+uri+"\"}");
    }
}
