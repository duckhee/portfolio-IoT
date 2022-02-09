package kr.co.won.util.file;

import kr.co.won.blog.domain.BlogResourceDomain;
import kr.co.won.blog.persistence.BlogResourcePersistence;
import kr.co.won.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component(value = "multiPartsFile")
@RequiredArgsConstructor
public class FileUtilImpl implements FileUtil {

    private final AppProperties appProperties;

    private final ModelMapper modelMapper;



}
