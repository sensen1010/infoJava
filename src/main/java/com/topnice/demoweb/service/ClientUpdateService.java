package com.topnice.demoweb.service;

import com.topnice.demoweb.entity.ClientUpdate;

public interface ClientUpdateService {

    //添加版本
    ClientUpdate add(ClientUpdate clientUpdate);

    //查询更新列表
    String findALL(String page, String size);

    //查询最后一个
    String findLastOne();

}
