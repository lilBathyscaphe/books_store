package com.epam.bookstore.service;

import com.epam.bookstore.dto.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;


//https://spring.io/guides/gs/uploading-files/
//https://github.com/spring-guides/gs-uploading-files/blob/main/complete/src/main/java/com/example/uploadingfiles/storage/FileSystemStorageService.javaz
@Slf4j
@Component
public class FileSystemStorageService {

    private static final String EMPTY_IMAGE_MESSAGE_KEY = "error.book.imageSave.empty";
    private static final String WRONG_PATH_MESSAGE_KEY = "error.book.imageSave.wrongPath";
    private static final String SAVE_IMAGE_FAIL_MESSAGE_KEY = "error.book.imageSave.fail";
    private static final String STORAGE_FAIL_MESSAGE_KEY = "error.book.imageSave.storageFail";
    private final ResourceBundleMessageSource messageResourceBundle;

    @Value("${application.storage.baseDir}")
    private String previewImageDir;
    private Path rootLocation;


    @Autowired
    public FileSystemStorageService(ResourceBundleMessageSource messageResourceBundle) {
        this.messageResourceBundle = messageResourceBundle;
    }

    @PostConstruct
    public void init() {
        try {
            rootLocation = Paths.get(previewImageDir);
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            log.error("Could not initialize storage", e);
            throw new ServiceException(
                    messageResourceBundle.getMessage(STORAGE_FAIL_MESSAGE_KEY, null, Locale.US), e);
        }
    }

    public void store(MultipartFile file, Book createdBook) {
        try {
            log.debug("Image file is: {}", file);

            if (file.isEmpty()) {
                throw new ServiceException(
                        messageResourceBundle.getMessage(EMPTY_IMAGE_MESSAGE_KEY, null, Locale.US));
            }

            Path destinationFile = Paths.get(createdBook.getPreviewImg()).normalize().toAbsolutePath();
            log.debug("Image save location: {}", destinationFile);
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new ServiceException(
                        messageResourceBundle.getMessage(WRONG_PATH_MESSAGE_KEY, null, Locale.US));
            }
            Files.write(destinationFile, file.getBytes());
        } catch (IOException e) {
            log.error("Cannot store file because", e);
            throw new ServiceException(
                    messageResourceBundle.getMessage(SAVE_IMAGE_FAIL_MESSAGE_KEY, null, Locale.US), e);
        }
    }


}
