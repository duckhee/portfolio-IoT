package kr.co.won.util.file;

import kr.co.won.blog.domain.BlogResourceDomain;
import kr.co.won.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component(value = "multiPartsFile")
@RequiredArgsConstructor
public class FileUtilImpl implements FileUtil {

    private final AppProperties appProperties;

    private final ModelMapper modelMapper;

    @Override
    public void fileUpload(MultipartFile file) throws IOException {
        BlogResourceDomain blogFileResource = BlogResourceDomain.builder()
                .fileSize(String.valueOf(file.getSize()))
                .originalName(file.getOriginalFilename())
                .extension(StringUtils.getFilenameExtension(file.getOriginalFilename()))
                .build();
        //  save name make
        blogFileResource.generateSaveName();
        File newUploadFile = new File(appProperties.getUploadFolderPath() + "/" + blogFileResource.getSaveFileName());
        file.transferTo(newUploadFile);
    }

    @Override
    public void fileUpload(List<MultipartFile> files) throws IOException {
        String uploadFolderPath = appProperties.getUploadFolderPath();
        for (MultipartFile file : files) {
            BlogResourceDomain blogFileResource = BlogResourceDomain.builder()
                    .fileSize(String.valueOf(file.getSize()))
                    .originalName(file.getOriginalFilename())
                    .extension(StringUtils.getFilenameExtension(file.getOriginalFilename()))
                    .build();
            //  save name make
            blogFileResource.generateSaveName();
            File newUploadFile = new File(appProperties.getUploadFolderPath() + "/" + blogFileResource.getSaveFileName());

            file.transferTo(newUploadFile);


            file.getName();
            file.getOriginalFilename();
            file.getContentType();

        }
    }

    @Override
    public void fileUpload(ServletFileUpload servletFileUpload) {
        FileUtil.super.fileUpload(servletFileUpload);
    }
}
