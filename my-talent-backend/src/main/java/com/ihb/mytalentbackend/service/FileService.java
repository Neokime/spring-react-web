package com.ihb.mytalentbackend.service;

import com.ihb.mytalentbackend.dto.UploadFileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    UploadFileDTO upload(MultipartFile file);
}
