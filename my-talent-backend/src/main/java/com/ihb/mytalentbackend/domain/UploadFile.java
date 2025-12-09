package com.ihb.mytalentbackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 원본 파일명
    @Column(nullable = false)
    private String originalName;

    // 서버에 저장되는 파일명 (UUID 등)
    @Column(nullable = false)
    private String storedName;

    // 접근 가능한 URL 경로 (/uploads/xxx.jpg)
    @Column(nullable = false)
    private String url;

    // 저장된 경로 (로컬 물리 경로)
    @Column(nullable = false)
    private String filePath;

    private Long fileSize;
}
