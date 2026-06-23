package com.tsms.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tsms.controller.FileService;
import com.tsms.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private Cloudinary cloudinary;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response<?> uploadFile(MultipartFile file) {
        try {

            if (file == null || file.isEmpty()) {
                return new Response<>(400, "File is required", null);
            }

            String contentType = file.getContentType();

            String folder;
            String resourceType;

            if (contentType != null && contentType.startsWith("image/")) {
                folder = "tsms/images";
                resourceType = "image";
            } else if ("application/pdf".equals(contentType)) {
                folder = "tsms/pdfs";
                resourceType = "raw";
            } else {
                return new Response<>(400,
                        "Only image and pdf files are allowed",
                        null);
            }

            String fileName = UUID.randomUUID().toString();

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", resourceType,
                            "folder", folder,
                            "public_id", fileName,
                            "overwrite", true,
                            "invalidate", true
                    )
            );

            String fileUrl = uploadResult.get("secure_url").toString();
            logger.info("upload result: {}", fileUrl);

            return new Response<>(200,
                    "File uploaded successfully",
                    fileUrl);

        } catch (Exception e) {
            return new Response<>(500,
                    e.getMessage(),
                    null);
        }
    }
}
