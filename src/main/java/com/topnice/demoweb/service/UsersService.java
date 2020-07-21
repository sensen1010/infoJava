package com.topnice.demoweb.service;

import com.topnice.demoweb.entity.Users;

public interface UsersService {
    Users login(Users users);

    Users findByUserId(String userID);

    Users add(Users users, String enterId);

    String findByNameList(String enterId, String name, String page, String size);

    Users findByName(String name);

    /**
     * 根据账号、企业id查询是否存在
     */
    Users findByUserNameAndEnterId(String userName, String enterId);

}
