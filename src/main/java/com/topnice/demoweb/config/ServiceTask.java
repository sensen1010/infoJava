package com.topnice.demoweb.config;

import com.topnice.demoweb.service.InfoMonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ServiceTask {

    @Autowired
    InfoMonService infoMonService;

    //每天凌晨2点执行(更新apk)
    @Scheduled(cron = "0 0 2 * * ?")
    public void apk() {
        //更新apk
        new Thread(new Runnable() {
            @Override
            public void run() {
                infoMonService.updateApkService();
            }
        }).start();
    }

    //每天凌晨3点执行（更新客户端）
    @Scheduled(cron = "0 0 3 * * ?")
    public void client() {
        //更新客户端
        new Thread(new Runnable() {
            @Override
            public void run() {
                infoMonService.updateClientService();
            }
        }).start();
    }
}
