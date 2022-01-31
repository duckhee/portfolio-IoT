package kr.co.won.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/blogs/{blogIdx}/reply")
@RequiredArgsConstructor
public class BlogReplyApiController {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    /**
     * blog service
     */
    private final BlogService blogService;


    @GetMapping
    public ResponseEntity listBlogRepliesResource(@PathVariable(name = "blogIdx") Long blogIdx) {
        return null;
    }

    @PostMapping
    public ResponseEntity createBlogRepliesResource(@PathVariable(name = "blogIdx") Long blogIdx) {
        return null;
    }

    @GetMapping(path = "/{replyIdx}")
    public ResponseEntity readBlogRepliesResource(@PathVariable(name = "blogIdx") Long blogIdx, @PathVariable(name = "replyIdx") Long replyIdx) {
        return null;
    }
}
