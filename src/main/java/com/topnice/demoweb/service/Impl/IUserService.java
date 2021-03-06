package com.topnice.demoweb.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.repository.EnterRepository;
import com.topnice.demoweb.repository.UsersRepository;
import com.topnice.demoweb.service.UsersService;
import com.topnice.demoweb.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class IUserService implements UsersService {


    @Autowired
    UsersRepository userRepository;

    @Autowired
    EnterRepository enterRepository;

    @Override
    public Users login(Users users) {
        System.out.println("进入登录service"+new Date());
        String userName = users.getUserName().trim();
        String md5Str = DigestUtils.md5DigestAsHex(users.getPow().trim().getBytes());
        String pow = DigestUtils.md5DigestAsHex(md5Str.getBytes());
        System.out.println("密码转化"+new Date());
        System.out.println("根据账号密码查询"+new Date());
        Users user = userRepository.findAllByUserNameAndPow(userName, pow);
        if (user == null) {
            return null;
        }
        System.out.println("用户状态为1返回"+new Date());
        if (user.getState().equals("1")){
            return null;
        }
        System.out.println("企业状态1返回"+new Date());
        Enterprise enterprise=enterRepository.findAllByEnterId(user.getEnterId());
        if (enterprise.getState().equals("1")){
            return null;
        }
        System.out.println("返回用户"+new Date());
        return user;
    }

    @Override
    public Users findByUserId(String userId) {
        return userRepository.findAllByUserId(userId);
    }

    @Override
    public Users add(Users users, String enterId) {
        Users re = userRepository.findAllByUserName(users.getUserName());
        if (re != null) {
            return null;
        }
        //加密
        String md5Str = DigestUtils.md5DigestAsHex(users.getPow().getBytes());
        String pow = DigestUtils.md5DigestAsHex(md5Str.getBytes());
        users.setPow(pow);
        //设置类型
        users.setType("2");
        users.setState("0");
        users.setCreationTime(new Date());
        users.setUserId(UUID.randomUUID().toString().replace("-", ""));
        if (enterId == null || enterId.equals("")) {
            users.setEnterId(users.getEnterId());
        } else {
            users.setEnterId(enterId);
        }
        return userRepository.save(users);
    }

    @Override
    public String findByNameList(String enterId, String name, String state, String page, String size) {
        name = name == null || name.equals("") ? "" : name;
        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");

        Page<Users> all = userRepository.findAllByEnterIdAndNameContainingAndState(enterId, name, state, pageable);

        List<Map<String, Object>> lists = new ArrayList<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        for (Users users : all) {
            Map<String, String> map = new HashMap<>();
            map.put("id", users.getId() + "");
            map.put("userName", users.getUserName());
            map.put("state", users.getState());
            map.put("name", users.getName());
            Enterprise enterprise = enterRepository.findAllByEnterId(users.getEnterId());
            if (enterprise != null) {
                map.put("enterName", enterprise.getEnterName());
            }
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
    public String adminFindByNameList(String enterId, String name, String state, String page, String size) {

        try {
            enterId = enterId == null || enterId.equals("") ? "" : enterId;
            name = name == null || name.equals("") ? "" : name;
            //如果为null默认为0
            Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
            //如果为null默认为10
            Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);
            Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");
            Page<Users> all = userRepository.findAllByEnterIdContainingAndNameContainingAndState(enterId, name, state, pageable);
            List<Map<String, Object>> lists = new ArrayList<>();
            List<Map<String, String>> list = new ArrayList<>();
            Map<String, Object> maps = new HashMap<>();
            for (Users users : all) {
                Map<String, String> map = new HashMap<>();
                map.put("id", users.getId() + "");
                map.put("userName", users.getUserName());
                map.put("state", users.getState());
                map.put("enterId", users.getEnterId());
                Enterprise enterprise = enterRepository.findAllByEnterId(users.getEnterId());
                if (enterprise != null) {
                    map.put("enterName", enterprise.getEnterName());
                }
                map.put("name", users.getName());
                map.put("creationTime", DateUtil.date2TimeStamp(users.getCreationTime(), "yyyy-MM-dd HH:mm:ss") + "");
                map.put("userId", users.getUserId());
                list.add(map);
            }
            maps.put("size", all.getTotalElements());
            maps.put("data", JSONObject.toJSONString(list));
            lists.add(maps);
            return JSONObject.toJSONString(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Users findByUserNameAndEnterId(String userName, String enterId) {
        Users re = userRepository.findAllByUserNameAndEnterId(userName, enterId);
        if (re != null) {
            return null;
        }
        return re;
    }

    @Override
    public Users findByUserIdAndEnterId(String userId, String enterId) {
        Users re = userRepository.findAllByUserIdAndEnterId(userId, enterId);
        if (re == null) {
            return null;
        }
        return re;
    }

    @Override
    public Users modifyUser(String userId,String enterId,String pow) {

        Enterprise enterprise=enterRepository.findAllByEnterId(enterId);
        if (enterprise==null){
            return null;
        }
        //根据企业id+用户id查询是否存在
        Users users=userRepository.findAllByUserId(userId);
        if (users==null){
            return null;
        }
        String md5Str = DigestUtils.md5DigestAsHex(pow.getBytes());
        String newPow = DigestUtils.md5DigestAsHex(md5Str.getBytes());
        users.setPow(newPow);
        return userRepository.saveAndFlush(users);
    }

    @Override
    public Users modifyUserState(String userId, String state, String enterId) {
        //根据企业id+用户id查询是否存在

        Users users=userRepository.findAllByUserIdAndEnterId(userId, enterId);
        if (users==null){
            return null;
        }
        users.setState(state);
        userRepository.saveAndFlush(users);
        return users;
    }

    @Override
    public Users findByUserName(String userName) {

        return userRepository.findAllByUserName(userName);
    }
}
