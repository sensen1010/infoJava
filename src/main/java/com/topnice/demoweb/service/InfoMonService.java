package com.topnice.demoweb.service;

public interface InfoMonService {
    //向云服务器提交信息
    boolean callService(String enterId);

    //向云服务器获取更新信息(获取apk)
    String updateApkService();

}
