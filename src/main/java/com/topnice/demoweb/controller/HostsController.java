package com.topnice.demoweb.controller;

import com.topnice.demoweb.entity.Hosts;
import com.topnice.demoweb.service.HostsService;
import com.topnice.demoweb.service.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private Map<String, Object> addHosts(String hostLinkId, String hostName) {
        map = new HashMap<>();
        Hosts hosts = hostsService.addHosts(hostLinkId, hostName);
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
    private Map<String, Object> selectHosts(String hostName, String hostState, String linkState, String page, String size) {
        map = new HashMap<>();
        String data = hostsService.selectHosts(hostName, hostState, linkState, page, size);
        WebSocketServer.selectAllUser();
        map.put("data", data + "");
        return map;
    }

    @ApiOperation(value = "/selectId", tags = "管理员查询主机列表")
    @RequestMapping("/selectId")
    private Map<String, Object> selectHostId(String linkId) {
        map = new HashMap<>();
        Hosts all = hostsService.selectHostId(linkId);
        System.out.println("进入查询");
        if (all == null) {
            map.put("code", "1");
            map.put("msg", "无数据");
            return map;
        }

        map.put("code", "0");
        map.put("msg", "查询成功");
        map.put("hostId", all.getHostId());
        return map;
    }

    @ApiOperation(value = "/userSelect", tags = "管理员查询主机列表")
    @RequestMapping("/userSelect")
    private Map<String, Object> userSelectHosts(String hostAdminID, String state, String page, String size) {
        map = new HashMap<>();
        String all = hostsService.selectHost(hostAdminID, state, page, size);
        if (all.equals("1")) {
            map.put("code", "1");
            map.put("msg", "无数据");
            return map;
        }
        map.put("code", "0");
        map.put("msg", "查询成功");
        map.put("data", all);
        return map;
    }

    @ApiOperation(value = "/update/", tags = "修改主机")
    @RequestMapping("/update/name")
    private Map<String, Object> updateHostsName(String hostId, String hostName) {
        map = new HashMap<>();
        Hosts hosts = hostsService.updateHostName(hostId, hostName);
        return mapMsg(hosts);
    }

    @ApiOperation(value = "/update/", tags = "修改主机")
    @RequestMapping("/update/state")
    private Map<String, Object> updateHostsState(String hostId, String state) {
        map = new HashMap<>();
        Hosts hosts = hostsService.updateHostState(hostId, state);
        return mapMsg(hosts);
    }

    Map<String, Object> mapMsg(Hosts hosts) {
        if (hosts == null) {
            map.put("code", "1");
            map.put("msg", "修改失败");
            return map;
        } else {
            map.put("code", "0");
            map.put("msg", "修改成功");
            map.put("data", hosts);
        }
        return map;
    }
}
