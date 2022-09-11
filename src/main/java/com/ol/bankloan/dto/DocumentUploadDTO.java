package com.ol.bankloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data @NoArgsConstructor @AllArgsConstructor
public class DocumentUploadDTO {
    private Long userId;
    private MultipartFile document;
    private String documentName;
    private String description;
}
