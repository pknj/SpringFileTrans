package com.pawel.filetrans.controllers;

import com.pawel.filetrans.dto.UploadStatusDTO;
import com.pawel.filetrans.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = {"/file"})
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public UploadStatusDTO handleFileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile) {

        if (!multipartFile.isEmpty()) {
            String filename = multipartFile.getOriginalFilename();
            try {
                fileService.saveFile(request, multipartFile);
                return getStatusDTO(filename, "OK", "");

            } catch (IOException e) {
                log.error("Unable to upload file: {}", filename);
                e.printStackTrace();
                return getStatusDTO(filename, "ERROR", "Unable to upload file!");
            }
        }
        return getStatusDTO("", "ERROR", "Empty file!");
    }

    @GetMapping("/greeting")
    public UploadStatusDTO greeting(@RequestParam(required=false, defaultValue="World") String name) {
        System.out.println("==== in greeting ====");
        return new UploadStatusDTO("1", name);
    }

    private UploadStatusDTO getStatusDTO(String fileName, String status, String description) {
        UploadStatusDTO statusDTO = new UploadStatusDTO();
        statusDTO.setFileName(fileName);
        statusDTO.setStatus(status);
        statusDTO.setErrorDescription(description);
        return statusDTO;
    }
}
