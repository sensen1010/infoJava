package com.topnice.demoweb.controller;


import com.topnice.demoweb.entity.ImgUrl;
import com.topnice.demoweb.exception.MyException;
import com.topnice.demoweb.service.FileUpService;
import com.topnice.demoweb.service.HostsImgService;
import com.topnice.demoweb.service.ImgUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/img", tags = {"图片操作接口"})
@RestController
@RequestMapping("/img")
public class ImgController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    ImgUrlService imgUrlService;

    @Autowired
    HostsImgService hostsImgService;

    @Autowired
    FileUpService fileUpService;


    Map<String, Object> myMap;
    private Logger logger = LoggerFactory.getLogger(ImgController.class);

    /**
     * @desc: 查询接口类
     * @author: sen
     * @date: 2020/6/18 0018 11:57
     **/
    @ApiOperation(value = "/imgList", notes = "分页模糊查询图片列表：名称、状态、显示状态")
    @RequestMapping("/imgList")
    private Map<String, Object> selectImgList(String name, String state, String showType, String page, String size) {
        myMap = new HashMap<>();
        myMap.put("data", imgUrlService.findAllByImgNameStateShow(name, state, showType, page, size));
        myMap.put("code", "0");
        return myMap;
    }

    @ApiOperation(value = "/isImgList", notes = "查询类型为jpg、png、MP4的文件")
    @RequestMapping("/isImgList")
    private Map<String, Object> selectIsImgList(String type, String page, String size) {
        myMap = new HashMap<>();
        myMap.put("data", imgUrlService.findAllByIsImgMp4(type, page, size));
        myMap.put("code", "0");
        return myMap;
    }

    @ApiOperation(value = "/pa", notes = "查询主机图片关联主机id，图片id，页数")
    @RequestMapping("/pa")
    private Map<String, Object> selectImg(String hostId, String imgId, String state, String page, String size) throws MyException {
        myMap = new HashMap<>();
        int rpage = Integer.parseInt(page);
        int rsize = Integer.parseInt(size);
        hostsImgService.findAllByHostImg(hostId, imgId);
        return myMap;
    }

    @ApiOperation(value = "/host/select", notes = "主机查询自己可查看的图片，主机id，state")
    @RequestMapping("/host/select")
    private Map<String, Object> hostSelectImg(String hostId, String state, String hostImgState) throws MyException {
        myMap = new HashMap<>();
        myMap.put("data", hostsImgService.findAllByHostIdImgUrl(hostId, state, hostImgState));
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
    private Map<String, Object> updateImg(String imgUrlId, String state) {
        myMap = new HashMap<>();
        ImgUrl imgUrl = imgUrlService.findAllByImgUrlId(imgUrlId);
        if (imgUrl == null || imgUrl.equals("")) {
            myMap.put("code", "1");
            myMap.put("msg", "图片不存在");
        } else {
            imgUrl.setState(state);
            imgUrlService.updateImgTime(imgUrl);
            myMap.put("code", "0");
            myMap.put("msg", "修改成功");
        }
        return myMap;
    }

    /**
     * @desc: 修改图片的显示类型、0为库存、1为全部、2为部分
     * @author: sen
     * @date: 2020/6/18 0018 13:45
     **/
    @ApiOperation(value = "/updateImg/show", notes = "修改图片表的显示类型 0为库存、1为全部显示、2为部分显示图片id，state")
    @RequestMapping("/updateImg/show")
    private Map<String, Object> updateImg(String imgUrlId, String showState, String[] hostsList) {
        myMap = new HashMap<>();
        String re = fileUpService.updateImg(imgUrlId, showState, hostsList);
        if (re.equals("1")) {
            myMap.put("code", "1");
            myMap.put("msg", "图片不存在");
        } else {
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
    @ApiOperation(value = "/up", notes = "图片上传接口")
    @RequestMapping("/up")
    public synchronized Map<String, Object> singleFileUpload(@RequestParam("file") MultipartFile[] reportFile, String type, String[] hostList) {
        myMap = new HashMap<>();
        myMap.put("code", "0");
        System.out.println("上传中");
        myMap.put("noImgList", fileUpService.saveFile(reportFile, type, hostList));
        System.out.println(myMap.toString());
        return myMap;
    }


}
