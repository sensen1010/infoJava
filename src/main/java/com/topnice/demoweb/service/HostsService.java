package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.Hosts;

public interface HostsService {

    //添加主机
    Hosts addHosts(String hostLinkId, String hostName);

    //根据管理员id查询主机列表、普通管理员
    String selectHost(String hostAdminID, String state, String page, String size);

    //超级管理员查询
    String selectHosts(String hostName, String state, String linkState, String page, String size);

    //修改主机的名称、普通管理员+超级管理员
    Hosts updateHostName(String hostId, String hostName);

    //修改主机的状态 超级管理员
    Hosts updateHostState(String hostId, String state);

    //修改主机链接状态
    Hosts updateHostLState(Hosts hosts);

    //根据主机Id查询是否存在
    Hosts selectHostId(String hostLinkId);
}
