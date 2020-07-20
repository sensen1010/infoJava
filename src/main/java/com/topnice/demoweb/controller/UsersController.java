package com.topnice.demoweb.controller;


import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.service.UsersService;
import com.topnice.demoweb.service.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
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

    private Map<String, String> mmap;

    @ApiOperation(value = "login", notes = "登录接口")
    @RequestMapping("/login")
    public Map<String, String> login(Users users) {
        System.out.println(users);
        mmap = new HashMap<>();
        String data = usersService.login(users);
        if (data == null) {

        }
        mmap.put("data", usersService.login(users) + "");
        return mmap;
    }

    @ApiOperation(value = "add", notes = "添加")
    @RequestMapping("/add")
    public Map<String, String> add(Users users) {
        mmap = new HashMap<>();
        Users users1 = usersService.addUsers(users);
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

    @ApiOperation(value = "select", notes = "查询所有")
    @RequestMapping("/select")
    public Map<String, String> select(String loginName, String page, String size) {
        mmap = new HashMap<>();
        String users1 = usersService.findAllByLoginName(loginName, page, size);
        mmap.put("data", users1 + "");
        return mmap;
    }

    @RequestMapping("all")
    public String selectAll() {
        String time = "网络时间：" + new Date();
        System.out.println("正在访问时间" + new Date());
        return time;
    }

    @RequestMapping("index")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("请求成功");
    }

    @RequestMapping("page")
    public ModelAndView page() {
        return new ModelAndView("websocket");
    }

    @RequestMapping("userall")
    public String userall(String message) throws IOException {
        WebSocketServer.senAllMessage(message);
        return "成功";
    }

    @RequestMapping("/push/{toUserId}")
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        System.out.println(message);
        WebSocketServer.sendInfo(message, toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }
}
