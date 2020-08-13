package com.topnice.demoweb.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUpService {

    String add(MultipartFile reportFile, String userId, String enterId);

    String clientApkAdd(MultipartFile reportFile, String modifyContent);

    String proImgAdd(MultipartFile reportFile, String enterId);

}
