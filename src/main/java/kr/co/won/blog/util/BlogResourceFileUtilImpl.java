package kr.co.won.blog.util;

import kr.co.won.blog.domain.BlogResourceDomain;
import kr.co.won.blog.persistence.BlogResourcePersistence;
import kr.co.won.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component(value = "blogMultiPartsFile")
@RequiredArgsConstructor
public class BlogResourceFileUtilImpl implements BlogResourceFileUtil {

    private final AppProperties appProperties;

    private final ModelMapper modelMapper;

    private final ServletContext servletContext;

    private final BlogResourcePersistence blogResourcePersistence;

    @Override
    public BlogResourceDomain fileUpload(MultipartFile file) throws IOException {
        BlogResourceDomain blogFileResource = BlogResourceDomain.builder()
                .fileSize(String.valueOf(file.getSize()))
                .originalName(file.getOriginalFilename())
                .extension(StringUtils.getFilenameExtension(file.getOriginalFilename()))
                .build();
        //  save name make
        blogFileResource.generateSaveName();
        // make file save path
        String contextPath = servletContext.getContextPath();
        log.info("servlet context path ::: {}", contextPath);
        String savedFilePath = appProperties.getUploadFolderPath() + "/" + blogFileResource.getSaveFileName();
        File newUploadFile = new File(savedFilePath);
        // folder not have make folder
        if(!newUploadFile.exists()){
            newUploadFile.mkdirs();
        }
        file.transferTo(newUploadFile);
        BlogResourceDomain savedBlogResource = blogResourcePersistence.save(blogFileResource);
        return savedBlogResource;
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
        BlogResourceFileUtil.super.fileUpload(servletFileUpload);
    }
}
