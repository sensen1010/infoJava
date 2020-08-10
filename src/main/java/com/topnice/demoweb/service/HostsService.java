package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.Hosts;

public interface HostsService {

    //添加主机
    Hosts add(String hostIp,String hostLinkId, String hostName, String enterId);

    //根据企业id查询主机列表
    String findHost(String enterId, String hostName,String linkState,String state, String page, String size);

    //超级管理员查询
    String findHosts(String hostName, String state, String linkState, String page, String size);

    //修改主机的名称、普通管理员+超级管理员
    Hosts modifyHostName(String hostId, String hostName);

    //修改主机的状态 超级管理员
    Hosts modifyHostState(String hostId, String state);

    //修改主机链接状态
    Hosts modifyHostLState(Hosts hosts);

    //根据主机Id查询是否存在
    Hosts findHostId(String hostLinkId);

    //根据企业id、连接状态、状态查询
    String findHostList(String enterId,String linkState,String state);

    //根据企业id、链接id是否存在
    Hosts findEnterIdAndHostLinkId(String enterId,String hostLinkId);
}
