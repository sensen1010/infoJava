package com.topnice.demoweb.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.ClientUpdate;
import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.entity.FileUrl;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.repository.ClientUpdateRepository;
import com.topnice.demoweb.service.*;
import com.topnice.demoweb.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class IFileUpService implements FileUpService {


    private String UPLOAD_FOLDER = System.getProperty("user.dir");
    private String PATH = "\\webapps\\file\\";

    @Autowired
    FileUrlService fileUrlService;

    @Autowired
    UsersService usersService;

    @Autowired
    EnterpriseService enterpriseService;

    @Autowired
    ClientUpdateRepository clientUpdateRepository;

    @Autowired
    ClientUpdateService clientUpdateService;
    Map<String, Object> myMap;

    @Override
    public String add(MultipartFile reportFile, String userId, String enterId) {
        //Users users = usersService.findByUserId(userId);
        Users users = usersService.findByUserId(userId);
        if (users == null) {
            return null;
        }
        Enterprise enterprise = enterpriseService.findByEnterId(enterId);
        if (enterprise == null) {
            return null;
        }
        List<Map<String, String>> list = new ArrayList<>();

            Map<String, String> m = new HashMap<>();
            String fileMd5 = null;
            try {
                fileMd5 = FileUtil.fileToBetyArray(reportFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                //保存图片信息
                //查询该图是否存在，若存在，则更新该图片修改日期
                FileUrl reFileUrl = fileUrlService.findByFileMD5(fileMd5);
                if (reFileUrl == null || reFileUrl.equals("")) {
                    //上传文件的名称
                    String upFileName = reportFile.getOriginalFilename();
                    //截取末尾类型
                    int lastFile = upFileName.lastIndexOf(".");
                    String upFileType = upFileName.substring(lastFile + 1, upFileName.length());
                    //判断文件归类
                    String numFile = FileUtil.fileType(upFileType);
                    if (numFile == null) {
                        return null;
                    }
                    //判断存储位置
                    String fileTypePath = FileUtil.fileTypePath(numFile);
                    //新的存储位置
                    String newFilePath = enterId + "/" + fileTypePath+"/";
                    //文件名
                    String fileName = fileMd5 + "." + upFileType;
                    String uuidFile = UUID.randomUUID().toString().replace("-", "");
                    //设置文件路径，
                    int lastURL = UPLOAD_FOLDER.lastIndexOf("\\");
                    String upFileUrl = UPLOAD_FOLDER.substring(0, lastURL) + PATH;
                    String filePath = upFileUrl + newFilePath;
                    File targetFile = new File(filePath);
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    File oldFile = new File(filePath + fileName);
                    FileUrl fileUrl = new FileUrl();
                    reportFile.transferTo(oldFile);
                    //FileTypeJudge.isFileType(FileTypeJudge.getType(new FileInputStream(oldFile)));
                    if (numFile.equals("3")) {
                        FileUtil.getTempPath(filePath + "/img/" + fileMd5 + ".jpg", filePath + fileName);
                        fileUrl.setVideoImg(newFilePath + "/img/" + fileMd5 + ".jpg");
                    }
                    //写入信息
                    fileUrl.setFileTypeId(numFile);
                    fileUrl.setEnterId(enterId);
                    fileUrl.setUserId(userId);
                    fileUrl.setState("0");//显示状态 0为正常
                    fileUrl.setFileSize(reportFile.getSize()+"");
                    fileUrl.setFileType(upFileType);
                    fileUrl.setFileMd5(fileMd5);
                    fileUrl.setFileName(reportFile.getOriginalFilename());
                    fileUrl.setFileUrl(newFilePath + "/" + fileName);
                    fileUrl.setFileUrlId(uuidFile);
                    fileUrl.setUpdateTime(new Date());
                    fileUrl.setCreationTime(new Date());
                    fileUrlService.add(fileUrl);
                } else {
                    reFileUrl.setUpdateTime(new Date());
                    m.put("name", reportFile.getOriginalFilename());
                    m.put("md5", fileMd5);
                    list.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

        return JSONObject.toJSONString(list);
    }

    @Override
    public String clientApkAdd(MultipartFile reportFile, String modifyContent) {

        //上传文件的名称
        String upApkName = reportFile.getOriginalFilename();
        //截取末尾类型
        int lastFile = upApkName.lastIndexOf(".");
        String upApkType = upApkName.substring(lastFile + 1, upApkName.length()).toLowerCase();
        System.out.println(upApkType);
        if (!upApkType.equals("apk")) {
            return null;
        }
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> m = new HashMap<>();
        String apkMd5 = null;
        try {
            apkMd5 = FileUtil.fileToBetyArray(reportFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //  System.out.println(apkMd5);
            //查询该apk是否存在，若存在，则添加记录、不上传文件
            List<ClientUpdate> clientUpdate = clientUpdateRepository.findAllByApkMd5(apkMd5);
            if (clientUpdate.size() < 1) {
                //文件名
                String fileName = apkMd5 + "." + upApkType;
                String uuidFile = UUID.randomUUID().toString().replace("-", "");
                //设置文件路径，
                int lastURL = UPLOAD_FOLDER.lastIndexOf("\\");
                String upFileUrl = UPLOAD_FOLDER.substring(0, lastURL) + PATH;
                String filePath = upFileUrl;
                File targetFile = new File(filePath);
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                File oldFile = new File(filePath + fileName);
                reportFile.transferTo(oldFile);
                //写入信息
                ClientUpdate client = new ClientUpdate();
                client.setApkMd5(apkMd5);
                client.setApkName(upApkName);
                client.setClientId(uuidFile);
                client.setDownloadUrl(fileName);
                client.setModifyContent(modifyContent);
                client.setApkSize(reportFile.getSize() + "");
                client.setUpdateTime(new Date());
                clientUpdateService.add(client);
            } else {
                //写入信息
                ClientUpdate client = new ClientUpdate();
                client.setApkMd5(clientUpdate.get(0).getApkMd5());
                client.setApkName(clientUpdate.get(0).getApkName());
                client.setClientId(UUID.randomUUID().toString().replace("-", ""));
                client.setDownloadUrl(clientUpdate.get(0).getDownloadUrl());
                client.setModifyContent(modifyContent);
                client.setApkSize(clientUpdate.get(0).getApkSize() + "");
                client.setUpdateTime(new Date());
                clientUpdateService.add(client);
            }
            m.put("name", reportFile.getOriginalFilename());
            m.put("md5", apkMd5);
            list.add(m);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return JSONObject.toJSONString(list);
    }

    @Override
    public String proImgAdd(MultipartFile reportFile, String enterId) {
        Enterprise enterprise = enterpriseService.findByEnterId(enterId);
        if (enterprise == null) {
            return null;
        }
        String newFilePath = null;
        try {
            //上传文件的名称
            String upFileName = reportFile.getOriginalFilename();
            //截取末尾类型
            int lastFile = upFileName.lastIndexOf(".");
            String upFileType = upFileName.substring(lastFile + 1, upFileName.length());
            //判断存储位置
            String fileTypePath = "program";
            //新的存储位置
            newFilePath = enterId + "/" + fileTypePath + "/";
            //文件名
            String uuidFile = UUID.randomUUID().toString().replace("-", "");
            String fileName = uuidFile + "." + upFileType;
            //设置文件路径，
            int lastURL = UPLOAD_FOLDER.lastIndexOf("\\");
            String upFileUrl = UPLOAD_FOLDER.substring(0, lastURL) + PATH;
            String filePath = upFileUrl + newFilePath;
            File targetFile = new File(filePath);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            File oldFile = new File(filePath + fileName);
            reportFile.transferTo(oldFile);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return newFilePath;
    }

}
