package com.pawel.springfiletrans.security.controller;

import com.pawel.springfiletrans.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = {"/admin/file"})
public class FileController {

  private final FileService fileService;

  @Autowired
  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile) {
    String message;

    if (!multipartFile.isEmpty()) {
      String filename = multipartFile.getOriginalFilename();
      try {
        fileService.saveFile(request, multipartFile);
        message = "File uploaded: " + filename;
        log.info(message);
        return ResponseEntity.status(HttpStatus.OK).body(message);
      } catch (IOException e) {
        message = "Unable to upload file: " + filename;
        log.error(message);
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(message);
      }
    }
    message = "Empty file!";
    log.error(message);
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(message);
  }
}
