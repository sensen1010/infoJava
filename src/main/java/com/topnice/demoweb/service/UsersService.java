package com.topnice.demoweb.service;

import com.topnice.demoweb.entity.Users;

public interface UsersService {
    Users login(Users users);

    Users findByUserId(String userID);

    Users add(Users users, String enterId);

    String findByLoginNameList(String enterId, String loginName, String page, String size);

    Users findByLoginName(String loginName);
}
