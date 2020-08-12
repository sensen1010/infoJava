package com.topnice.demoweb.util;

import com.topnice.demoweb.repository.HostsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @desc: 项目启动时执行
 * @author: sen
 * @date: 2020/8/11 0011 8:54
 **/

@Component
public class InitConfig implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(InitConfig.class);

    @Autowired
    HostsRepository hostsRepository;

    @Override
    public void run(ApplicationArguments args) {
        log.info("项目启动初始化开始ApplicationRunner");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(20);
                        hostsRepository.updateAllHostLinkState();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        //获取项目的绝对路径
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) path = new File("");
            System.out.println("path:" + path.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        log.info("项目启动初始化结束ApplicationRunner");
    }
}
