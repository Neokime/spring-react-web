package com.ihb.mytalentbackend.dto;

import lombok.Data;

@Data
public class UploadFileDTO {
    private Long id;
    private String originalName;
    private String storedName;
    private String url;
    private long fileSize;
}
