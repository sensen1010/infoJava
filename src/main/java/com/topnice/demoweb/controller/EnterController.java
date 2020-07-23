package com.topnice.demoweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.service.EnterpriseService;
import com.topnice.demoweb.token.annotation.UserLoginToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/enter", tags = {"企业管理接口"})
@RestController
@RequestMapping("/enter")
public class EnterController {

    @Autowired
    EnterpriseService enterpriseService;

    private Map<String, Object> map;


    @ApiOperation(value = "/enter添加企业", tags = "添加企业")
    @RequestMapping(value = "/enter", method = RequestMethod.POST)
    private Map<String, Object> addEnter(Enterprise enterprise) {
        map = new HashMap<>();
        String enter = enterpriseService.add(enterprise);
        if (enter == null) {
            map.put("code", "1");
            map.put("msg", "添加失败");
            return map;
        }
        map.put("code", "0");
        map.put("msg", "添加成功");
        map.put("data", enter);
        return map;
    }

    @UserLoginToken
    @ApiOperation(value = "/enter查询所有企业列表", tags = "根据名称,查询所有企业")
    @RequestMapping(value = "/enter/list", method = RequestMethod.GET)
    private Map<String, Object> selectEnterList(String state) {
        String re = enterpriseService.findEnterList(state);
        if (re == null) {
            map.put("code", "1");
            return map;
        }
        map = new HashMap<>();
        map.put("code", "0");
        map.put("data", re);
        return map;
    }

    @UserLoginToken
    @ApiOperation(value = "/enter根据名称,查询所有企业", tags = "根据名称,查询所有企业")
    @RequestMapping(value = "/enter", method = RequestMethod.GET)
    private Map<String, Object> selectEnter(String state, String enterName, String page, String size) {
        String re = enterpriseService.findByStateAndNameList(state, enterName, page, size);
        if (re == null) {
            map.put("code", "1");
            return map;
        }
        map = new HashMap<>();
        map.put("code", "0");
        map.put("data", re);
        return map;
    }

    @UserLoginToken
    @ApiOperation(value = "/enter/{enterId}根据id,查询某企业", tags = "根据id,查询某企业")
    @RequestMapping(value = "/enter/{enterId}", method = RequestMethod.GET)
    private Map<String, Object> selectEnterId(@PathVariable("enterId") String enterId) {
        map = new HashMap<>();
        Enterprise re = enterpriseService.findByEnterId(enterId);
        if (re == null) {
            map.put("cord", "1");
            map.put("msg", "查询失败");
            return map;
        }
        map.put("code", "0");
        map.put("msg", "查询成功");
        map.put("data", JSONObject.toJSONString(re) + "");
        return map;
    }


    @UserLoginToken
    @ApiOperation(value = "/enter/{enterId}根据id,删除某企业", tags = "根据id,删除某企业")
    @RequestMapping(value = "/enter/{enterId}", method = RequestMethod.DELETE)
    private Map<String, Object> delectEnterId(@PathVariable("enterId") String enterId, String state) {
        map = new HashMap<>();
        Enterprise re = enterpriseService.modifyEnterState(enterId, state);
        if (re == null) {
            map.put("cord", "1");
            map.put("msg", "操作失败");
            return map;
        }
        map.put("code", "0");
        map.put("msg", "成功");
        return map;
    }

    @ApiOperation(value = "/enter/{enterId}根据id,更新某企业", tags = "根据id,更新某企业")
    @RequestMapping(value = "/enter/{enterId}", method = RequestMethod.PATCH)
    private Map<String, Object> updateEnterId() {
        return map;
    }

}
