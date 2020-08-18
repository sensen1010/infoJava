package com.topnice.demoweb.controller;

import com.topnice.demoweb.entity.Hosts;
import com.topnice.demoweb.service.HostsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/host", tags = {"安卓机操作接口"})
@RestController
@RequestMapping("/host")
public class HostsController {

    @Autowired
    HostsService hostsService;

    private Map<String, Object> map;

    @ApiOperation(value = "/add", tags = "添加主机")
    @RequestMapping("/add")
    private Map<String, Object> addHosts(String hostIp,String hostLinkId, String hostName, String enterId, String userId) {
        map = new HashMap<>();
        Hosts hosts = hostsService.add(hostIp,hostLinkId, hostName, enterId);
        if (hosts == null) {
            map.put("code", "1");
            map.put("msg", "添加失败，主机已存在");
            return map;
        }
        map.put("code", "0");
        map.put("msg", "添加成功");
        map.put("data", hosts);
        return map;
    }
    @ApiOperation(value = "/select", tags = "超级管理员查询所有主机列表")
    @RequestMapping("/select")
    private Map<String, Object> selectHosts(String enterId, String hostName, String linkState, String page, String size) {
        map = new HashMap<>();
        String data = hostsService.findHosts(enterId, hostName, linkState, page, size);
        map.put("data", data + "");
        return map;
    }
    @ApiOperation(value = "/selectId", tags = "企业管理员根据连接Id查询主机列表")
    @RequestMapping("/selectId")
    private Map<String, Object> selectHostId(String linkId) {
        map = new HashMap<>();
        Hosts all = hostsService.findHostId(linkId);
        if (all == null) {
            map.put("code", "1");
            return map;
        }
        map.put("code", "0");
        map.put("hostId", all.getHostId());
        return map;
    }
    @ApiOperation(value = "/userSelect", tags = "企业管理员查询主机列表")
    @RequestMapping("/userSelect")
    private Map<String, Object> userSelectHosts(String enterId,String hostName,String linkState ,String state, String page, String size) {
        map = new HashMap<>();
        String all = hostsService.findHost(enterId, hostName,linkState,state, page, size);
        if (all.equals("1")) {
            map.put("code", "1");
            return map;
        }
        map.put("code", "0");
        map.put("data", all);
        return map;
    }
    @ApiOperation(value = "/user/hostList", tags = "企业管理员查询主机列表")
    @RequestMapping(value = "/user/hostList",method = RequestMethod.GET)
    private Map<String, Object> userHostsList(String enterId,String linkState,String state) {
        map = new HashMap<>();
        String all = hostsService.findHostList(enterId,"0","0");
        if (all.equals("1")) {
            map.put("code", "1");
            return map;
        }
        map.put("code", "0");
        map.put("data", all);
        return map;
    }
    @ApiOperation(value = "/update/", tags = "修改主机")
    @RequestMapping("/update/name")
    private Map<String, Object> updateHostsName(String hostId, String hostName) {
        map = new HashMap<>();
        Hosts hosts = hostsService.modifyHostName(hostId, hostName);
        return mapMsg(hosts);
    }
    @ApiOperation(value = "/update/", tags = "修改主机")
    @RequestMapping("/update/state")
    private Map<String, Object> updateHostsState(String hostId, String state) {
        map = new HashMap<>();
        Hosts hosts = hostsService.modifyHostState(hostId, state);
        return mapMsg(hosts);
    }

    @ApiOperation(value = "/delete/", tags = "删除主机")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    private Map<String, Object> deleteHost(String hostId) {
        map = new HashMap<>();
        hostsService.deleteHost(hostId);
        map.put("code", "0");
        return map;
    }



    Map<String, Object> mapMsg(Hosts hosts) {
        if (hosts == null) {
            map.put("code", "1");
            return map;
        } else {
            map.put("code", "0");
            map.put("data", hosts);
        }
        return map;
    }
}
