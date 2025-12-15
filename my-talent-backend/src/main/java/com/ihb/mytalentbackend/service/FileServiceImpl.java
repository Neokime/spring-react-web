package com.ihb.mytalentbackend.service;

import com.ihb.mytalentbackend.domain.UploadFile;
import com.ihb.mytalentbackend.dto.UploadFileDTO;
import com.ihb.mytalentbackend.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final UploadFileRepository uploadFileRepository;

    @Override
    public UploadFileDTO upload(MultipartFile file) {

        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isBlank()) {
                throw new RuntimeException("파일명이 비어 있습니다.");
            }

            // 프로젝트 루트 기준 /uploads
            String projectRoot = System.getProperty("user.dir");
            File uploadDir = new File(projectRoot, "uploads");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String storedName = System.currentTimeMillis() + "_" + originalName;
            File dest = new File(uploadDir, storedName);

            file.transferTo(dest);

            String url = "/uploads/" + storedName;

            UploadFile saved = uploadFileRepository.save(
                    UploadFile.builder()
                            .originalName(originalName)
                            .storedName(storedName)
                            .filePath(dest.getAbsolutePath())
                            .url(url)
                            .fileSize(file.getSize())
                            .build()
            );

            //  엔티티 → DTO
            UploadFileDTO dto = new UploadFileDTO();
            dto.setId(saved.getId());
            dto.setOriginalName(saved.getOriginalName());
            dto.setStoredName(saved.getStoredName());
            dto.setUrl(saved.getUrl());
            dto.setFileSize(saved.getFileSize());

            return dto;

        } catch (Exception e) {
            log.error("파일 업로드 실패", e);
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }
}
