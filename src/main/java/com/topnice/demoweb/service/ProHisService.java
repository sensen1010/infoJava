package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.ProHis;

public interface ProHisService {

    //添加历史
    ProHis addProHis(ProHis proHis, String[] hostList);

    //根据节目名称查询历史
    String findAllByName(String name, String page, String size);


}
