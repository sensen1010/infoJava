package com.topnice.demoweb.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.entity.FileUrl;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.service.EnterpriseService;
import com.topnice.demoweb.service.FileUpService;
import com.topnice.demoweb.service.FileUrlService;
import com.topnice.demoweb.service.UsersService;
import com.topnice.demoweb.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class IFileUpService implements FileUpService {

    @Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;

    @Autowired
    FileUrlService fileUrlService;

    @Autowired
    UsersService usersService;

    @Autowired
    EnterpriseService enterpriseService;

    Map<String, Object> myMap;

    @Override
    public String add(MultipartFile[] reportFile, String enterId, String userId) {
        //Users users = usersService.findByUserId(userId);
        Users users = usersService.findByUserIdAndEnterId(userId, enterId);
        if (users == null) {
            return null;
        }

        Enterprise enterprise = enterpriseService.findByEnterId(enterId);
        if (enterprise == null) {
            return null;
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (MultipartFile multipartFile : reportFile) {
            Map<String, String> m = new HashMap<>();
            String fileMd5 = null;
            try {
                fileMd5 = FileUtil.fileToBetyArray(multipartFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                //保存图片信息
                //查询该图是否存在，若存在，则更新该图片修改日期
                FileUrl reFileUrl = fileUrlService.findByFileMD5(fileMd5);
                if (reFileUrl == null || reFileUrl.equals("")) {
                    //上传文件的名称
                    String upFileName = multipartFile.getOriginalFilename();
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
                    String newFilePath = enterId + "/" + fileTypePath;
                    //文件名
                    String fileName = fileMd5 + "." + upFileType;
                    String uuidFile = UUID.randomUUID().toString().replace("-", "");
                    //设置文件路径，
                    String filePath = UPLOAD_FOLDER + newFilePath;
                    File targetFile = new File(filePath);
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    File oldFile = new File(filePath + fileName);
                    FileUrl fileUrl = new FileUrl();
                    multipartFile.transferTo(oldFile);
                    //FileTypeJudge.isFileType(FileTypeJudge.getType(new FileInputStream(oldFile)));
                    fileUrl.setState("0");//显示状态 0为正常
                    fileUrl.setFileType(upFileType);
                    fileUrl.setFileMd5(fileMd5);
                    fileUrl.setFileName(multipartFile.getOriginalFilename());
                    fileUrl.setFileUrl(newFilePath + "/" + fileName);
                    fileUrl.setFileUrlId(uuidFile);
                    fileUrl.setUpdateTime(new Date());
                    fileUrl.setCreationTime(new Date());
                    fileUrlService.add(fileUrl);
                } else {
                    reFileUrl.setUpdateTime(new Date());
                    m.put("name", multipartFile.getOriginalFilename());
                    m.put("md5", fileMd5);
                    list.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return JSONObject.toJSONString(list);
    }

}
