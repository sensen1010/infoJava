package com.topnice.demoweb.controller;


import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/users", tags = {"用户操作接口"})
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    private Map<String, String> mmap;

    @ApiOperation(value = "login", notes = "登录接口")
    @RequestMapping("/login")
    public Map<String, String> login(Users users) {
        System.out.println(users);
        mmap = new HashMap<>();
        String data = usersService.login(users);
        if (data == null) {
            mmap.put("cord", "1");
            return mmap;
        }
        mmap.put("cord", "0");
        mmap.put("data", data + "");
        return mmap;
    }

    @ApiOperation(value = "add", notes = "添加")
    @RequestMapping("/add")
    public Map<String, String> add(Users users, String enterId) {
        mmap = new HashMap<>();
        Users users1 = usersService.add(users, enterId);
        if (users1 == null) {
            mmap.put("code", "1");
            mmap.put("msg", "添加失败");
        } else {
            mmap.put("code", "0");
            mmap.put("msg", "添加成功");
            mmap.put("data", users1 + "");
        }
        return mmap;
    }

    @ApiOperation(value = "select", notes = "企业管理员查询所有")
    @RequestMapping("/select")
    public Map<String, String> select(String enterId, String loginName, String page, String size) {
        mmap = new HashMap<>();
        String users1 = usersService.findByLoginNameList(enterId, loginName, page, size);
        mmap.put("data", users1 + "");
        return mmap;
    }

}
