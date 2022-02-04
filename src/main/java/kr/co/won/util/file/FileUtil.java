package kr.co.won.util.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUtil {

    public default void fileUpload(List<MultipartFile> files) {
        return;
    }

    public default MultipartFile fileDownload(String path) {
        return null;
    }
}
