package com.pawel.filetrans.service.impl;

import com.pawel.filetrans.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class DefaultFileService implements FileService {
    private final String uploadDir;

    @Autowired
    public DefaultFileService(@Value("${fileUpload.data.dir}")String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public void saveFile(HttpServletRequest request, MultipartFile multipartFile) throws IOException {
        String realPathToUploads = request.getServletContext().getRealPath(uploadDir);
        if (!new File(realPathToUploads).exists()) {
            new File(realPathToUploads).mkdir();
        }
        log.info("realPathToUploads: {}", realPathToUploads);
        String orgName = multipartFile.getOriginalFilename();
        String filePath = realPathToUploads + orgName;
        multipartFile.transferTo(new File(filePath));
    }
}
