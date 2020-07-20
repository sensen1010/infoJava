package com.topnice.demoweb.service;

import com.topnice.demoweb.entity.Users;

public interface UsersService {
    String login(Users users);

    Users findAllByUserId(String userID);

    Users addUsers(Users users);

    String findAllByLoginName(String loginName, String page, String size);

}
