package com.topnice.demoweb.controller;


import com.topnice.demoweb.entity.InfoLayout;
import com.topnice.demoweb.entity.ProHis;
import com.topnice.demoweb.entity.Program;
import com.topnice.demoweb.service.InfoLayoutService;
import com.topnice.demoweb.service.ProHisService;
import com.topnice.demoweb.service.ProgramService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/program", tags = {"信息发布接口"})
@RestController
@RequestMapping("/program")
public class ProgramController {

    @Autowired
    InfoLayoutService infoLayoutService;

    @Autowired
    ProgramService programService;

    @Autowired
    ProHisService proHisService;

    Map<String, String> map;

    @ApiOperation(value = "/layout/add", notes = "添加布局")
    @RequestMapping("/layout/add")
    private Map<String, String> addLayout(InfoLayout infoLayout) {
        map = new HashMap<>();
        map.put("data", infoLayoutService.addLayout(infoLayout) + "");
        return map;
    }

    @ApiOperation(value = "/layout/select", notes = "查询布局")
    @RequestMapping("/layout/select")
    private Map<String, String> selectLayout(String type) {
        map = new HashMap<>();
        map.put("data", infoLayoutService.findAll(type) + "");
        return map;
    }


    @ApiOperation(value = "/pro/add", notes = "添加节目")
    @RequestMapping("/pro/add")
    private Map<String, String> addProgram(Program program) {
        map = new HashMap<>();
        System.out.println(program);
        map.put("data", programService.addProgram(program) + "");
        return map;
    }

    @ApiOperation(value = "/pro/select", notes = "查询节目")
    @RequestMapping("/pro/select")
    private Map<String, String> selectProgram(String name, String page, String size) {
        map = new HashMap<>();
        map.put("data", programService.findAllByName(name, page, size) + "");
        return map;
    }

    @ApiOperation(value = "/proHis/add", notes = "发布节目")
    @RequestMapping("/proHis/add")
    private Map<String, String> addProHis(ProHis proHis, String[] hostList) {
        map = new HashMap<>();
        map.put("data", proHisService.addProHis(proHis, hostList) + "");
        return map;
    }

    @ApiOperation(value = "/proHis/select", notes = "查询发布节目历史")
    @RequestMapping("/proHis/select")
    private Map<String, String> selectProHis(String name, String page, String size) {
        map = new HashMap<>();
        map.put("data", proHisService.findAllByName(name, page, size));
        return map;
    }


}
