package com.topnice.demoweb.service;

import com.topnice.demoweb.entity.Users;

public interface UsersService {

    /**
     * 登录接口
     */
    Users login(Users users);
    /**
     * 根据账号id查询是否存在
     */
    Users findByUserId(String userID);
    /**
     * 添加账号
     */
    Users add(Users users, String enterId);
    /**
     * 企业接口  根据企业id、登录名、分页查询数据
     */
    String findByNameList(String enterId, String name, String state, String page, String size);
    /**
     * 超级管理员接口  根据企业id、登录名、分页查询数据
     */
    String adminFindByNameList(String enterId, String name, String state, String page, String size);
    /**
     * 根据账号、企业id查询是否存在
     */
    Users findByUserNameAndEnterId(String userName, String enterId);
    /**
     * 根据用户id、企业id查询是否存在
     */
    Users findByUserIdAndEnterId(String userId, String enterId);

    Users modifyUser(String userId,String user);

}
