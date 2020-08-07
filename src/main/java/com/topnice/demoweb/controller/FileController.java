package com.topnice.demoweb.controller;


import com.topnice.demoweb.entity.FileUrl;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.service.FileUpService;
import com.topnice.demoweb.service.FileUrlService;
import com.topnice.demoweb.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/file", tags = {"文件操作接口"})
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    FileUrlService fileUrlService;

    @Autowired
    FileUpService fileUpService;

    @Autowired
    UsersService usersService;


    Map<String, Object> myMap;
    private Logger logger = LoggerFactory.getLogger(FileController.class);
    /**
     * @desc: 查询接口类
     * @author: sen
     * @date: 2020/6/18 0018 11:57
     **/
    @ApiOperation(value = "企业管理员查询文件", notes = "根据企业Id分页模糊查询图片列表：名称、状态、显示状态")
    @RequestMapping(value = "/fileList",method = RequestMethod.GET)
    private Map<String, Object> selectFileList(String enterId, String userId, String name, String fileTypeId, String state, String page, String size) {
        myMap = new HashMap<>();
        myMap.put("data", fileUrlService.findByFileNameAndStateAndType(enterId, name, fileTypeId, state, page, size));
        myMap.put("code", "0");
        return myMap;
    }
    /**
     * @desc: 超级管理员查询
     * @author: sen
     * @date: 2020/7/23 0023 17:20
     **/
    @ApiOperation(value = "超级管理员查询所有文件", notes = "根据企业Id分页模糊查询图片列表：名称、状态、显示状态")
    @RequestMapping(value = "/admin/fileList",method = RequestMethod.GET)
    private Map<String, Object> adminSelectFileList(String enterId, String userId, String name, String fileTypeId, String state, String page, String size) {
        myMap = new HashMap<>();
        //判断用户类型
        Users users=usersService.findByUserId(userId);
        if (users==null||!users.getType().equals("0")){
            myMap.put("code", "1");
            return myMap;
        }
        myMap.put("data", fileUrlService.adminFindByFileNameAndStateAndType(enterId, name, fileTypeId, state, page, size));
        myMap.put("code", "0");
        return myMap;
    }
    @ApiOperation(value = "/fileTyList", notes = "查询类型为jpg、png、MP4的文件")
    @RequestMapping("/fileTyList")
    private Map<String, Object> selectIsImgList(String typeId, String page, String size) {
        myMap = new HashMap<>();
        myMap.put("data", fileUrlService.findByFileTypeId(typeId, page, size));
        myMap.put("code", "0");
        return myMap;
    }
    /**
     * @desc: 修改类
     * @author: sen
     * @date: 2020/6/18 0018 12:00
     **/
    /**
     * @desc: 删除文件、禁用文件、恢复文件
     * @author: sen
     * @date: 2020/6/18 0018 12:30
     **/
    @ApiOperation(value = "/update/state", notes = "修改图片表的状态,0正常 1禁用 2删除主机id，state")
    @RequestMapping("/update/state")
    private Map<String, Object> updateImg(String fileUrlId, String state) {
        myMap = new HashMap<>();
        FileUrl fileUrl = fileUrlService.findByFileUrlId(fileUrlId);
        if (fileUrl == null || fileUrl.equals("")) {
            myMap.put("code", "1");
        } else {
            fileUrl.setState(state);
            fileUrlService.modifyFileTime(fileUrl);
            myMap.put("code", "0");
        }
        return myMap;
    }
    /**
     * @desc: 上传方法
     * @author: sen
     * @date: 2020/6/18 0018 11:57
     **/
    @ApiOperation(value = "文件上传", notes = "文件上传接口")
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public synchronized Map<String, Object> singleFileUpload(@RequestParam("file") MultipartFile reportFile, String userId, String enterId) {
        myMap = new HashMap<>();
        String re = fileUpService.add(reportFile, userId, enterId);
        if (re == null) {
            myMap.put("code", "1");
            myMap.put("msg", reportFile.getOriginalFilename());
            return myMap;
        }
        myMap.put("code", "0");
        return myMap;
    }

    /**
     * @desc: 保存截图
     * @author: sen
     * @date: 2020/8/5 0005 9:25
     **/
    @ApiOperation(value = "文件上传", notes = "文件上传接口")
    @RequestMapping(value = "/proFile", method = RequestMethod.POST)
    public synchronized Map<String, Object> proFileUpload(@RequestParam("file") MultipartFile reportFile, String enterId) {
        myMap = new HashMap<>();
        String re = fileUpService.proImgAdd(reportFile, enterId);
        if (re == null) {
            myMap.put("code", "1");
            return myMap;
        }
        myMap.put("code", "0");
        myMap.put("data", re);
        return myMap;
    }
}
