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
    @Value("${prop.upload-news}")
    private String UPLOAD_NEWS;

    @Autowired
    FileUrlService fileUrlService;

    @Autowired
    UsersService usersService;

    @Autowired
    EnterpriseService enterpriseService;

    Map<String, Object> myMap;

    @Override
    public String add(MultipartFile[] reportFile, String userId) {

        Users users = usersService.findByUserId(userId);
        if (users == null) {
            return null;
        }

        Enterprise enterprise = enterpriseService.findByEnterId(users.getEnterId());
        if (enterprise == null) {
            return null;
        }

        List<Map<String, String>> list = new ArrayList<>();
        for (MultipartFile multipartFile : reportFile) {
            Map<String, String> m = new HashMap<>();
            String[] fileType = multipartFile.getOriginalFilename().split("\\.");
            String uuidImg = UUID.randomUUID().toString().replace("-", "");
            String filePath = UPLOAD_FOLDER;
            File targetFile = new File(filePath);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            String Md5Name = null;
            try {
                Md5Name = FileUtil.fileToBetyArray(multipartFile.getInputStream());
                System.out.println(Md5Name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                //保存图片信息
                //查询该图是否存在，若存在，则更新该图片修改日期
                FileUrl reFileUrl = fileUrlService.findByFileMD5(Md5Name);
                String fileName = Md5Name + "." + fileType[fileType.length - 1];
                File oldFile = new File(filePath + fileName);
                FileUrl fileUrl = new FileUrl();
                if (reFileUrl == null || reFileUrl.equals("")) {
                    multipartFile.transferTo(oldFile);
                    //FileTypeJudge.isFileType(FileTypeJudge.getType(new FileInputStream(oldFile)));
                    String[] imgName = {"jpg", "png", "mp4", "jpeg"};
                    fileUrl.setState("0");//显示状态 0为正常
                    fileUrl.setImgType(fileType[fileType.length - 1]);
                    fileUrl.setFileMd5(Md5Name);
                    fileUrl.setFileName(multipartFile.getOriginalFilename());
                    fileUrl.setFileUrl(fileName);
                    fileUrl.setFileUrlId(uuidImg);
                    fileUrl.setUpdateTime(new Date());
                    fileUrl.setCreationTime(new Date());
                    fileUrlService.add(fileUrl);
                } else {
                    reFileUrl.setUpdateTime(new Date());
                    m.put("name", multipartFile.getOriginalFilename());
                    m.put("md5", Md5Name);
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
