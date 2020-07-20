package com.topnice.demoweb.service;

import com.topnice.demoweb.entity.Enterprise;

public interface EnterpriseService {

    //保存企业信息
    String add(Enterprise enterprise);

    //根据名称、状态分页查询企业
    String findByStateAndNameList(String state, String name, String page, String size);

    //根据id查询企业信息
    Enterprise findByEnterId(String enterId);

    //修改企业信息
    Enterprise modifyEnter(Enterprise enterprise);

}
