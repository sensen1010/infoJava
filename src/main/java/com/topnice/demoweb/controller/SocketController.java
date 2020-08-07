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
    public String userall(String enterId, String message) throws IOException {
        WebSocketServer.senAllMessage(enterId, message);
        return "成功";
    }

    @RequestMapping("/push/{toUserId}")
    public ResponseEntity<String> pushToWeb(String message, String enterId, @PathVariable String toUserId) throws IOException {
        System.out.println(message);
        WebSocketServer.sendInfo(message, enterId, toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

    @RequestMapping("/enterHostAll")
    public ResponseEntity<String> pushToEnter(String enterId) throws IOException {
        System.out.println(WebSocketServer.enterHostAll(enterId));
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

}
