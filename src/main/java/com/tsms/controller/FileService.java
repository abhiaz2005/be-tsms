package com.tsms.controller;

import com.tsms.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Response<?> uploadFile(MultipartFile file);
}
