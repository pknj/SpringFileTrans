package com.pawel.springfiletrans.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadStatusDTO implements Serializable {

    public UploadStatusDTO() {
        //default constructor
    }

    public UploadStatusDTO(String status, String errorDescription) {
        this.status = status;
        this.errorDescription = errorDescription;
    }

    private static final long serialVersionUID = -4136536898743579179L;

    private String status;

    private String fileName;

    private String errorDescription;

}
