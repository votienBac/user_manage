package com.example.testintern.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileUploadService {
    File upload(MultipartFile imageFile);
}
