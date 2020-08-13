package com.topnice.demoweb.controller;


import com.topnice.demoweb.service.ClientUpdateService;
import com.topnice.demoweb.service.FileUpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/clientUpdate", tags = {"客户端更新接口"})
@RestController
@RequestMapping("/clientUpdate")
public class ClientUpdateController {

    private Map<String, Object> map;

    @Autowired
    FileUpService fileUpService;

    @Autowired
    ClientUpdateService clientUpdateService;

    /**
     * @desc: 客户端更新
     * @author: sen
     * @date: 2020/8/13 0013 11:01
     **/
    @ApiOperation(value = "客户端文件上传", notes = "文件上传接口")
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public synchronized Map<String, Object> singleFileUpload(@RequestParam("file") MultipartFile file, String modifyContent) {
        map = new HashMap<>();
        String name = fileUpService.clientApkAdd(file, modifyContent);
        if (name == null) {
            map.put("code", "1");
            return map;
        }
        map.put("code", "0");
        return map;
    }

    /**
     * @desc: 查询更新记录
     * @author: sen
     * @date: 2020/8/13 0013 14:26
     **/
    @ApiOperation(value = "查询更新记录", notes = "查询更新记录")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public synchronized Map<String, Object> select(String page, String size) {
        map = new HashMap<>();
        String data = clientUpdateService.findALL(page, size);
        if (data == null) {
            map.put("code", "1");
            return map;
        }
        map.put("code", "0");
        map.put("data", data);
        return map;
    }

    /**
     * @desc: 查询最新一条
     * @author: sen
     * @date: 2020/8/13 0013 14:26
     **/
    @ApiOperation(value = "查询最新更新记录", notes = "查询更新记录")
    @RequestMapping(value = "/newUpdate", method = RequestMethod.GET)
    public synchronized Map<String, Object> selectNew() {
        map = new HashMap<>();
        String data = clientUpdateService.findLastOne();
        if (data == null) {
            map.put("code", "1");
            return map;
        }
        map.put("code", "0");
        map.put("data", data);
        return map;
    }

}
