package com.topnice.demoweb.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/admin", tags = {"超级管理员操作接口"})
@RestController
@RequestMapping("/admin")
public class SuperAdminController {


}
