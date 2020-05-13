package com.leyou.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author lizichen
 * @create 2020-04-01 21:27
 */
public interface UploadService {
    String uploadImage(MultipartFile file);
}
