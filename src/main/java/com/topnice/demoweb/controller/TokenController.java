package com.topnice.demoweb.controller;


import com.topnice.demoweb.token.annotation.UserLoginToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "token接口", tags = {"/token"})
@RestController
@RequestMapping("/token")
public class TokenController {

    @UserLoginToken
    @ApiOperation(value = "判断token是否过期", notes = "token")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    synchronized public Map<String, String> token() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "0");
        return map;
    }

}
