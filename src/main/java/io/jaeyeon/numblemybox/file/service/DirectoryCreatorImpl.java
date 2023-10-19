package io.jaeyeon.numblemybox.file.service;

import io.jaeyeon.numblemybox.exception.ErrorCode;
import io.jaeyeon.numblemybox.exception.FileServiceException;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DirectoryCreatorImpl implements DirectoryCreator {

  @Override
  public void createDirectoryIfNotExists(String path) {
    File directory = new File(path);
    if (!directory.exists()) {
      // 디렉토리가 없으면 생성합니다.
      boolean dirCreated = directory.mkdirs();
      if (!dirCreated) {
        log.error("Failed to create directory: {}", path);
        throw new FileServiceException(ErrorCode.DIRECTORY_CREATION_FAILED);
      }
    }
  }
}