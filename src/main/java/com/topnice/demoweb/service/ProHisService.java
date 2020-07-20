package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.ProHis;

public interface ProHisService {

    //添加历史
    ProHis add(ProHis proHis);

    //根据节目名称查询历史
    String findByName(String name, String page, String size);


}
