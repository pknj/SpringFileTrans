package com.pawel.springfiletrans.service.impl;

import com.pawel.springfiletrans.exception.StorageException;
import com.pawel.springfiletrans.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefaultFileService implements FileService {
  private final Path path;

  @Autowired
  public DefaultFileService(@Value("${fileUpload.data.dir}") String uploadDir) {
    this.path = Paths.get(uploadDir);
  }

  @Override
  public void saveFile(HttpServletRequest request, MultipartFile file) {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());

    if (!path.toFile().exists()) {
      try {
        Files.createDirectories(path);
      } catch (IOException e) {
        log.error(e.getMessage());
      }
    }

    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + filename);
      }
      if (filename.contains("..")) {
        // This is a security check
        throw new StorageException(
            "Cannot store file with relative path outside current directory "
                + filename);
      }
      Files.copy(file.getInputStream(), this.path.resolve(filename),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + filename, e);
    }
  }
}
