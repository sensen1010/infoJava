package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.ProHis;

public interface ProHisService {

    //添加历史
    ProHis add(String enterId,String userId,String proId,String type,String[] hostList);

    //根据节目名称查询历史
    String findByName(String name, String page, String size);

    //企业根据节目名称查询历史
    String enterFindByName(String enterId,String name,String page,String size);

    //超级管理员查询节目
    String adminFindByName(String enterId, String name, String page, String size);


}
