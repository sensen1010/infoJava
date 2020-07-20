package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.HostsImg;
import com.topnice.demoweb.exception.MyException;

import java.util.List;

public interface HostsImgService {


    /**
     * @desc: 保存主机-图片关联信息
     * @author: sen
     * @date: 2020/6/18 0018 7:51
     **/
    HostsImg saveHostImg(HostsImg hostsImg) throws MyException;

    /**
     * @desc: 根据主机id+图片id+图片状态查询
     * @author: sen
     * @date: 2020/6/17 0017 18:15
     **/
    HostsImg findAllByHostImg(String hostId, String imgId) throws MyException;

    /**
     * @desc: 根据主机id查询
     * @author: sen
     * @date: 2020/6/18 0018 9:03
     **/
    HostsImg findAllByHost(String host);

    /**
     * @desc: 根据主机id查询关联图片状态为正常+图片表状态为正常的
     * @author: sen
     * @date: 2020/6/18 0018 10:44
     **/
    String findAllByHostIdImgUrl(String hostId, String state, String hostImgState);

    //根据图片id查询
    List<HostsImg> findAllByImgUrlId(String imgUrlId);

    HostsImg updateHostImgState(HostsImg hostsImg);
}
