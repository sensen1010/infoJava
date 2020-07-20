package com.topnice.demoweb.controller;

import com.topnice.demoweb.service.WebSocketServer;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/msg", tags = {"消息操作接口"})
@RestController
@RequestMapping("/msg")
public class SocketController {

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
