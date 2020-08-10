package com.topnice.demoweb.controller;


import com.topnice.demoweb.entity.ProHis;
import com.topnice.demoweb.entity.Program;
import com.topnice.demoweb.service.ProHisService;
import com.topnice.demoweb.service.ProgramService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/program", tags = {"信息发布接口"})
@RestController
@RequestMapping("/program")
public class ProgramController {



    @Autowired
    ProgramService programService;

    @Autowired
    ProHisService proHisService;

    Map<String, String> map;


    @ApiOperation(value = "/pro/add", notes = "添加节目")
    @RequestMapping("/pro/add")
    private Map<String, String> addProgram(Program program) {
        map = new HashMap<>();
        System.out.println(program);
        Program program1 = programService.add(program);
        map.put("code", "0");
        map.put("proId", program1.getProId() + "");
        return map;
    }

    @ApiOperation(value = "/pro/{proId}", notes = "更新节目")
    @RequestMapping(value = "/pro/{proId}", method = RequestMethod.PATCH)
    private Map<String, String> updateProgram(@PathVariable("proId") String proId, String enterId, String content, String contentHtml) {
        map = new HashMap<>();
        Program program1 = programService.modifyPro(proId, enterId, content, contentHtml);
        if (program1 == null) {
            map.put("code", "1");
            return map;
        } else {
            map.put("code", "0");
            map.put("proId", program1.getProId() + "");
        }
        return map;
    }

    @ApiOperation(value = "/pro/{enterId},根据企业Id查询节目", notes = "查询节目")
    @RequestMapping(value = "/pro/{enterId}", method = RequestMethod.GET)
    private Map<String, String> enterSelectProgram(@PathVariable("enterId") String enterId, String name, String page, String size) {
        map = new HashMap<>();
        map.put("data", programService.enterFindByName(enterId, name, page, size) + "");
        return map;
    }

    @ApiOperation(value = "/pro/{enterId},根据企业Id、节目id查询节目", notes = "查询节目")
    @RequestMapping(value = "/pro/{enterId}/{programId}", method = RequestMethod.GET)
    private Map<String, String> enterSelectProgramById(@PathVariable("enterId") String enterId, @PathVariable("programId") String programId) {
        map = new HashMap<>();
        map.put("data", programService.enterFindByProgramId(enterId, programId) + "");
        return map;
    }

    @ApiOperation(value = "/proHis/add", notes = "企业发布节目")
    @RequestMapping("/proHis/add")
    private Map<String, String> addProHis(String enterId,String userId,String proId,String type,String[] hostList) {
        map = new HashMap<>();
        ProHis proHis= proHisService.add(enterId,userId,proId,type,hostList);
        if (proHis==null){
            map.put("code","1");
            return map;
        }
        map.put("code","0");
        map.put("data",proHis+"");
        return map;
    }

    @ApiOperation(value = "/proHis/select", notes = "查询发布节目历史")
    @RequestMapping("/proHis/select")
    private Map<String, String> selectProHis(String enterId,String name, String page, String size) {
        map = new HashMap<>();
        map.put("data", proHisService.enterFindByName(enterId,name,page,size));
        return map;
    }


}
