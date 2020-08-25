package com.topnice.demoweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.service.EnterpriseService;
import com.topnice.demoweb.service.InfoMonService;
import com.topnice.demoweb.service.UsersService;
import com.topnice.demoweb.token.annotation.UserLoginToken;
import com.topnice.demoweb.token.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/users", tags = {"用户操作接口"})
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    TokenService tokenService;

    @Autowired
    EnterpriseService enterpriseService;

    @Autowired
    InfoMonService infoMonService;

    private Map<String, String> mmap;

    @ApiOperation(value = "login", notes = "登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, String> login(Users users) {
        mmap = new HashMap<>();
        //返回  1：账号密码错误  2：使用时间超时 0：登录成功
        System.out.println("进入登录asd"+new Date());
        Users data = usersService.login(users);
        if (data == null) {
            mmap.put("code", "1");
            return mmap;
        } else {
            System.out.println("进入checkEnter"+new Date());
            boolean check = enterpriseService.checkEnter(data.getEnterId());
            if (!check) {
                mmap.put("code", "2");
                return mmap;
            }
                String token = tokenService.getToken(data);
                mmap.put("code", "0");
                mmap.put("token", token);
                Map<String, String> map = new HashMap<>();
                map.put("name", data.getName());
                map.put("enterId", data.getEnterId());
                map.put("userName", data.getUserName());
                map.put("userId", data.getUserId());
                map.put("userType", data.getType());
                mmap.put("data", JSONObject.toJSONString(map));
            //向服务器推送记录
            new Thread(new Runnable() {
                @Override
                public void run() {
                    infoMonService.callService(data.getEnterId());
                }
            }).start();
            //infoMonService.callService(data.getEnterId());

            // infoMonService.updateApkService();
            return mmap;
        }
    }

    @UserLoginToken
    @ApiOperation(value = "/users", notes = "添加")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public Map<String, String> add(Users users, String enterId) {
        mmap = new HashMap<>();
        Users users1 = usersService.add(users, null);
        if (users1 == null) {
            mmap.put("code", "1");
        } else {
            mmap.put("code", "0");
        }
        return mmap;
    }

    @UserLoginToken
    @ApiOperation(value = "/users/{userId}", notes = "修改用户信息")
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PATCH)
    public Map<String, String> update(@PathVariable("userId")String userId,String pow,String enterId) {
        mmap = new HashMap<>();
        Users users1 = usersService.modifyUser(userId, enterId, pow);
        if (users1 == null) {
            mmap.put("code", "1");
        } else {
            mmap.put("code", "0");
        }
        return mmap;
    }

    @UserLoginToken
    @ApiOperation(value = "/users/{userId}", notes = "删除用户")
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    public Map<String, String> delete(@PathVariable("userId") String userId,String state,String enterId) {
        mmap = new HashMap<>();
        System.out.println(userId + "#########" + state + "#########" + enterId);
        Users users1 = usersService.modifyUserState(userId, state, enterId);
        if (users1 == null) {
            mmap.put("code", "1");
            return mmap;
        } else {
            mmap.put("code", "0");
        }
        return mmap;
    }

    @UserLoginToken
    @ApiOperation(value = "企业超级管理员查询用户接口", notes = "查询用户接口")
    @RequestMapping(value = "/enterUser", method = RequestMethod.GET)
    public Map<String, String> UserSelect(String name, String enterId, String state, String page, String size) {
        mmap = new HashMap<>();
        String users1 = usersService.findByNameList(enterId, name, state, page, size);
        if (users1 == null) {
            mmap.put("code", "1");
        } else {
            mmap.put("code", "0");
            mmap.put("data", users1 + "");
        }
        return mmap;
    }

    @UserLoginToken
    @ApiOperation(value = "超级管理员查询", notes = "企业管理员查询所有")
    @RequestMapping(value = "/adminUser", method = RequestMethod.GET)
    public Map<String, String> select(String name, String enterId, String state, String page, String size) {
        mmap = new HashMap<>();
        String reuser = usersService.adminFindByNameList(enterId, name, state, page, size);
        if (reuser == null) {
            mmap.put("code", "1");
        } else {
            mmap.put("code", "0");
            mmap.put("data", reuser + "");
        }
        return mmap;
    }

}
