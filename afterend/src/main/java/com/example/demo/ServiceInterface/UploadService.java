package com.example.demo.ServiceInterface;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    public String uploadSource(MultipartFile file) throws Exception;
}
