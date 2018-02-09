package com.pawel.filetrans.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface FileService {
    void saveFile(HttpServletRequest request, MultipartFile multipartFile) throws IOException;
}
