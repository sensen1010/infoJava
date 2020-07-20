package com.topnice.demoweb.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.HostsImg;
import com.topnice.demoweb.entity.ImgUrl;
import com.topnice.demoweb.exception.MyException;
import com.topnice.demoweb.service.FileUpService;
import com.topnice.demoweb.service.HostsImgService;
import com.topnice.demoweb.service.ImgUrlService;
import com.topnice.demoweb.util.FileUtil;
import com.topnice.demoweb.util.ListUtil;
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
    ImgUrlService imgUrlService;

    @Autowired
    HostsImgService hostsImgService;

    Map<String, Object> myMap;

    @Override
    public String saveFile(MultipartFile[] reportFile, String type, String[] hostList) {
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
                ImgUrl reimgUrl = imgUrlService.findImgByImgMD5(Md5Name);
                String fileName = Md5Name + "." + fileType[fileType.length - 1];
                File oldFile = new File(filePath + fileName);
                ImgUrl imgUrl = new ImgUrl();
                if (reimgUrl == null || reimgUrl.equals("")) {
                    multipartFile.transferTo(oldFile);
                    //FileTypeJudge.isFileType(FileTypeJudge.getType(new FileInputStream(oldFile)));
                    String[] imgName = {"jpg", "png", "mp4", "jpeg"};
                    imgUrl.setFileType("1");
                    for (int i = 0; i < imgName.length; i++) {
                        if (fileType[fileType.length - 1].equals(imgName[i])) {
                            imgUrl.setFileType("0");
                            break;
                        }
                    }
                    imgUrl.setState("0");//显示状态 0为正常
                    imgUrl.setImgType(fileType[fileType.length - 1]);
                    imgUrl.setImgMd5(Md5Name);
                    imgUrl.setImgName(multipartFile.getOriginalFilename());
                    imgUrl.setImgUrl(fileName);
                    imgUrl.setImgUrlId(uuidImg);
                    imgUrl.setShowType(type);//显示类型 0为在仓库  1为全部显示 2为部分
                    imgUrl.setUpdateTime(new Date());
                    imgUrl.setCreationTime(new Date());
                    imgUrlService.saveImg(imgUrl);
                    if (type == "2" || type.equals("2")) {
                        for (String hostId : hostList) {
                            //查询是否存在主机+图片关联
                            HostsImg rehostsImg = hostsImgService.findAllByHostImg(hostId, uuidImg);
                            if (rehostsImg == null || rehostsImg.equals("")) {
                                judgeHostImg(uuidImg, hostId);
                            } else {
                                rehostsImg.setState("0");
                                hostsImgService.updateHostImgState(rehostsImg);
                            }
                        }
                    }
                } else {
                    reimgUrl.setUpdateTime(new Date());
                    m.put("name", multipartFile.getOriginalFilename());
                    m.put("md5", Md5Name);
                    imgUrlService.updateImgTime(reimgUrl);
                    list.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return JSONObject.toJSONString(list);
    }

    @Override
    public String updateImg(String imgUrlId, String showState, String[] hostsList) {
        try {
            ImgUrl imgUrl = imgUrlService.findAllByImgUrlId(imgUrlId);
            if (imgUrl == null || imgUrl.equals("")) {
                return "1";
            } else {
                //修改图片状态
                showState = showState == null || showState.equals("") ? "0" : showState;
                imgUrl.setShowType(showState);
                imgUrlService.updateImgTime(imgUrl);
                //判断显示类型，0为在仓库 1为全部可见  2为部分可见
                if (showState == "2" || showState.equals("2")) {
                    //查询主机--图片关联表
                    List<HostsImg> imgList = hostsImgService.findAllByImgUrlId(imgUrlId);
                    List<String> imgHostList = new ArrayList<>();
                    //获取到主机列表
                    for (HostsImg hostsImg : imgList) {
                        imgHostList.add(hostsImg.getHostId());
                    }
                    //将主机数据转换为数组
                    String[] toimgHostList = imgHostList.toArray(new String[imgHostList.size()]);

                    //1.查询到的数组与传过来的数组的交集
                    String[] toImgHost = ListUtil.intersect(hostsList, toimgHostList);
                    //修改状态
                    for (String all : toImgHost) {
                        HostsImg rehostsImg = hostsImgService.findAllByHostImg(all, imgUrlId);
                        rehostsImg.setState("0");
                        hostsImgService.updateHostImgState(rehostsImg);
                    }
                    //2.根据交集，查询两个的差集
                    //与数据库图片表的差值,根据差值，可以修改差值的数据
                    String[] imgMinus = ListUtil.minus(toImgHost, toimgHostList);
                    for (String all : imgMinus) {
                        HostsImg rehostsImg = hostsImgService.findAllByHostImg(all, imgUrlId);
                        rehostsImg.setState("2");
                        hostsImgService.updateHostImgState(rehostsImg);
                    }
                    //与传过来的主机表判断,添加关联
                    String[] hostsMinus = ListUtil.minus(hostsList, toImgHost);
                    for (String all : hostsMinus) {
                        judgeHostImg(all, imgUrlId);
                    }
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return "0";
    }

    /**
     * @desc: 判断是否存在记录，不存在则添加。存在则修改状态
     * @author: sen
     * @date: 2020/6/18 0018 14:06
     **/
    private void judgeHostImg(String hostId, String ingUrlId) throws MyException {
        HostsImg hostsImg = new HostsImg();
        hostsImg.setState("0");
        hostsImg.setImgUrlId(ingUrlId);
        hostsImg.setHostsImgId(UUID.randomUUID().toString().replace(".", ""));
        hostsImg.setHostId(hostId);
        hostsImg.setCreationTime(new Date());
        hostsImgService.saveHostImg(hostsImg);
    }
}
