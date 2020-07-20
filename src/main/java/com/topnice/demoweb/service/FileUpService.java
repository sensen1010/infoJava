package com.topnice.demoweb.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUpService {

    String saveFile(MultipartFile[] reportFile, String type, String[] hostList);


    String updateImg(String imgUrlId, String showState, String[] hostsList);

}
