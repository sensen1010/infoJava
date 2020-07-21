package com.topnice.demoweb.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.repository.UsersRepository;
import com.topnice.demoweb.service.UsersService;
import com.topnice.demoweb.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class IUserService implements UsersService {


    @Autowired
    UsersRepository userRepository;

    @Override
    public Users login(Users users) {
        Users user = userRepository.findAllByUserNameAndPassword(users.getUserName(), users.getPassword());
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public Users findByUserId(String userId) {
        return userRepository.findAllByUserId(userId);
    }

    @Override
    public Users add(Users users, String enterId) {
        Users re = userRepository.findAllByLoginName(users.getLoginName());
        if (re != null) {
            return null;
        }
        users.setType("2");
        users.setCreationTime(new Date());
        users.setUserId(UUID.randomUUID().toString().replace("-", ""));
        users.setEnterId(enterId);
        return userRepository.save(users);
    }

    @Override
    public String findByLoginNameList(String enterId, String loginName, String page, String size) {

        loginName = loginName == null || loginName.equals("") ? "" : loginName;
        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");

        Page<Users> all = userRepository.findAllByEnterIdAndLoginNameContaining(enterId, loginName, pageable);

        List<Map<String, Object>> lists = new ArrayList<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        for (Users users : all) {
            Map<String, String> map = new HashMap<>();
            map.put("id", users.getId() + "");
            map.put("userName", users.getUserName());
            map.put("loginName", users.getLoginName());
            map.put("creationTime", DateUtil.date2TimeStamp(users.getCreationTime(), "yyyy-MM-dd HH:mm:ss") + "");
            map.put("userId", users.getUserId());
            list.add(map);
        }
        maps.put("size", all.getTotalElements());
        maps.put("data", JSONObject.toJSONString(list));
        lists.add(maps);
        return JSONObject.toJSONString(lists);
    }

    @Override
    public Users findByLoginName(String loginName) {
        Users re = userRepository.findAllByLoginName(loginName);
        if (re != null) {
            return null;
        }
        return re;
    }
}
