package kr.co.won.blog.util;

import kr.co.won.blog.domain.BlogResourceDomain;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BlogResourceFileUtil {

    public default BlogResourceDomain fileUpload(MultipartFile file) throws IOException {
        return null;
    }

    public default void fileUpload(List<MultipartFile> files) throws IOException {
        return;
    }

    public default void fileUpload(ServletFileUpload servletFileUpload) {
        return;
    }
/*
    public default MultipartFile fileDownload(String path) {
        return null;
    }

    public default File fileDownload(String path) {
        return null;
    }
    */
}
