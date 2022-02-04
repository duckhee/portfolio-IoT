package kr.co.won.util.file;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileUtil {

    public default void fileUpload(MultipartFile file) throws IOException {
        return;
    }

    public default void fileUpload(List<MultipartFile> files) throws IOException  {
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
