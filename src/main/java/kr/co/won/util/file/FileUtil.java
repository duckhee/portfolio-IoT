package kr.co.won.util.file;

import kr.co.won.blog.domain.BlogResourceDomain;
import kr.co.won.user.domain.UserDomain;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileUtil {
    /** Using MultipartFile */

    /**
     * Upload File Using Multipart File
     */
    public default void fileUpload(MultipartFile file) throws IOException {
        return;
    }

    public default void fileUpload(List<MultipartFile> files) throws IOException {
        return;
    }

    public default void fileUpload(ServletFileUpload servletFileUpload) {
        return;
    }

    /** Upload Encoding Base64 Encoding File */

    /**
     * Upload File Encoding BASE 64
     */

    public default boolean fileUpload(Class<?> file, String imageBase64) {
        return false;
    }

    /**
     * Login User Check and Upload File Encoding BASE 64
     */
    public default boolean fileUpload(UserDomain loginUser, Class<?> file, String imageBase64) {
        return false;
    }

    /** Download Encoding Base64 Encoding File */

    /**
     * Download File Encoding BASE 64
     */
    public default File fileDownload(Long fileIdx) throws IOException {
        return null;
    }

    /**
     * Download File Login Check And Encoding BASE 64
     */
    public default File fileDownload(UserDomain loginUser, Class<?> fileIdx) throws IOException {
        return null;
    }

    /*

    public default void fileUpload(File file) throws IOException {
        return;
    }

    public default void fileUpload(List<File> files) throws IOException {
        return;
    }

    public default void fileUpload(File servletFileUpload) {
        return;
    }

     */

/*
    public default MultipartFile fileDownload(String path) {
        return null;
    }

    public default File fileDownload(String path) {
        return null;
    }
    */
}
