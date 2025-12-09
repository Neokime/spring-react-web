package com.ihb.mytalentbackend.controller;

import com.ihb.mytalentbackend.dto.UploadFileDTO;
import com.ihb.mytalentbackend.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<UploadFileDTO> upload(@RequestParam("file") MultipartFile file) {
        UploadFileDTO dto = fileService.upload(file);
        return ResponseEntity.ok(dto);
    }
}
