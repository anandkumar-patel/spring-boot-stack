package anand.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class FileUploadDownloadConfig {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    void createUploadDir() {
        new File(uploadDir).mkdir();
    }
}
