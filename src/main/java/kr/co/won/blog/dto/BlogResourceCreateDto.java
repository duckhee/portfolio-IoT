package kr.co.won.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogResourceCreateDto {

    private String filename;

    private int uploaded;

    private URI url;

    private String savedName;
}
