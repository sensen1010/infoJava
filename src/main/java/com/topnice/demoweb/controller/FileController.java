package com.topnice.demoweb.controller;


import com.topnice.demoweb.entity.FileUrl;
import com.topnice.demoweb.service.FileUpService;
import com.topnice.demoweb.service.FileUrlService;
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


    Map<String, Object> myMap;
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * @desc: 查询接口类
     * @author: sen
     * @date: 2020/6/18 0018 11:57
     **/
    @ApiOperation(value = "企业管理员查询文件", notes = "根据企业Id分页模糊查询图片列表：名称、状态、显示状态")
    @RequestMapping(value = "/fileList",method = RequestMethod.GET)
    private Map<String, Object> selectImgList(String enterId,String name, String state, String page, String size) {
        myMap = new HashMap<>();
        myMap.put("data", fileUrlService.findByFileNameAndState(enterId, name, state, page, size));
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
    private Map<String, Object> selectImgList(String enterId,String userId,String name, String state, String page, String size) {
        myMap = new HashMap<>();
        myMap.put("data", fileUrlService.findByFileNameAndState(enterId, name, state, page, size));
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
            myMap.put("msg", "文件不存在");
        } else {
            fileUrl.setState(state);
            fileUrlService.modifyFileTime(fileUrl);
            myMap.put("code", "0");
            myMap.put("msg", "修改成功");
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
            myMap.put("msg", reportFile.getOriginalFilename()+"上传失败");
            return myMap;
        }
        myMap.put("code", "0");
        myMap.put("msg", "上传成功");
        return myMap;
    }


}
