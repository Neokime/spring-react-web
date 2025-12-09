// src/main/java/com/ihb/mytalentbackend/repository/UploadFileRepository.java
package com.ihb.mytalentbackend.repository;

import com.ihb.mytalentbackend.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}
