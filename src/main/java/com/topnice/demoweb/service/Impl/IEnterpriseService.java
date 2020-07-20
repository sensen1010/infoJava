package com.topnice.demoweb.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.repository.EnterRepository;
import com.topnice.demoweb.repository.UsersRepository;
import com.topnice.demoweb.service.EnterpriseService;
import com.topnice.demoweb.service.UsersService;
import com.topnice.demoweb.util.DateUtil;
import org.apache.commons.lang.RandomStringUtils;
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
public class IEnterpriseService implements EnterpriseService {

    @Autowired
    EnterRepository enterRepository;
    @Autowired
    UsersService usersService;
    @Autowired
    UsersRepository usersRepository;

    @Override
    public String add(Enterprise enterprise) {
        //添加企业信息
        enterprise.setEnterId(UUID.randomUUID().toString().replace("-", ""));
        Enterprise reEnter = enterRepository.save(enterprise);
        //根据企业信息，添加一个默认账号
        String loginName;
        while (true) {
            loginName = RandomStringUtils.randomAlphanumeric(8);
            Users reUsers = usersService.findByLoginName(loginName);
            if (reEnter == null) {
                break;
            }
        }
        Users users = new Users();
        users.setEnterId(reEnter.getEnterId());
        users.setCreationTime(new Date());
        users.setUserName(enterprise.getEnterName());
        users.setType("1");
        users.setLoginName(loginName);
        users.setPassword("123456");
        users.setUserId(UUID.randomUUID().toString().replace("-", ""));

        Users re = usersRepository.save(users);
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("userId", re.getUserId());
        map.put("userName", re.getUserName());
        map.put("loginName", re.getLoginName());
        map.put("pow", re.getPassword());
        map.put("type", re.getType());
        list.add(map);
        return JSONObject.toJSONString(list);
    }

    @Override
    public String findByStateAndNameList(String state, String name, String page, String size) {

        state = state == null || state.equals("") ? "0" : state;
        name = name == null || name.equals("") ? "" : name;

        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);

        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");

        Page<Enterprise> all = enterRepository.findAllByStateContainingAndEnterNameContaining(state, name, pageable);

        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> maps = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();
        for (Enterprise enter : all) {
            Map<String, String> map = new HashMap<>();
            map.put("enterId", enter.getEnterId());
            map.put("enterName", enter.getEnterName());
            map.put("hostNum", enter.getHostNum());
            map.put("enterAuth", enter.getEnterAuth());
            map.put("creationTime", DateUtil.date2TimeStamp(enter.getCreationTime(), "yyyy-MM-dd HH:mm"));
            map.put("state", enter.getState() + "");
            map.put("enterIp", enter.getEnterIp());
            list.add(map);
        }
        maps.put("size", all.getTotalElements() + "");
        maps.put("data", JSONObject.toJSONString(list));
        lists.add(maps);
        return JSONObject.toJSONString(lists);
    }

    @Override
    public Enterprise findByEnterId(String enterId) {

        if (enterId == null || enterId.equals("")) return null;

        Enterprise enterprise = enterRepository.findAllByEnterId(enterId);

        return enterprise;
    }

    @Override
    public Enterprise modifyEnter(Enterprise enterprise) {


        return null;
    }
}
