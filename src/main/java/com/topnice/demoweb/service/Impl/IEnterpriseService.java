package com.topnice.demoweb.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.repository.EnterRepository;
import com.topnice.demoweb.repository.UsersRepository;
import com.topnice.demoweb.service.EnterpriseService;
import com.topnice.demoweb.service.UsersService;
import com.topnice.demoweb.util.DateUtil;
import com.topnice.demoweb.util.EnterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class IEnterpriseService implements EnterpriseService {

    @Autowired
    EnterRepository enterRepository;
    @Autowired
    UsersService usersService;
    @Autowired
    UsersRepository usersRepository;

    @Value("${info.type}")
    private String TYPE;


    @Override
    public String add(Enterprise enterprise, String userName, String pow) {
        //内网、外网版判断  若是内网、只能创建一个账号
        if (!TYPE.equals("INTERNET")) {
            List<Enterprise> enterprises = enterRepository.findAll();
            if (enterprise != null) {
                return "100";
            }
        }

        //判断账号是否存在
        Users reUsers = usersRepository.findAllByUserName(userName);
        if (reUsers != null) {
            return "101";
        }

        //添加企业信息
        enterprise.setEnterId(UUID.randomUUID().toString().replace("-", ""));
        int date = DateUtil.timeStamp();
        //设置 3天5台机
        enterprise.setEnterDayAuth(EnterUtil.encryption("3"));
        enterprise.setHostNumAuth(EnterUtil.encryption("5"));
        enterprise.setEnterTimeAuth(EnterUtil.encryption(date + ""));
        enterprise.setCreationTime(new Date());
        enterprise.setState("0");

        Enterprise reEnter = enterRepository.save(enterprise);


        Users users = new Users();
        users.setEnterId(reEnter.getEnterId());
        users.setCreationTime(new Date());
        users.setUserName(userName);
        users.setType("1");
        users.setState("0");
        users.setName(reEnter.getEnterName());
        //加密
        String md5Str = DigestUtils.md5DigestAsHex(pow.getBytes());
        String encrypPow = DigestUtils.md5DigestAsHex(md5Str.getBytes());
        users.setPassword(encrypPow);
        users.setUserId(UUID.randomUUID().toString().replace("-", ""));
        Users re = usersRepository.save(users);
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("userId", re.getUserId());
        map.put("userName", re.getUserName());
        map.put("name", re.getName());
        map.put("pow", re.getPassword());
        map.put("type", re.getType());
        list.add(map);
        //更新保存默认账号id
        reEnter.setDefaultUserId(re.getUserId());
        enterRepository.saveAndFlush(reEnter);
        return JSONObject.toJSONString(list);
    }

    @Override
    public String adminAdd(String enterName, String hostNum, String day, String userName, String pow, String userId) {
        Users reUser = usersRepository.findAllByUserId(userId);
        if (!reUser.getType().equals("0")) {
            return null;
        }

        //判断账号是否存在
        Users reUsers = usersRepository.findAllByUserName(userName);
        if (reUsers != null) {
            return "101";
        }

        Enterprise enterprise = new Enterprise();
        enterprise.setEnterName(enterName);
        enterprise.setHostNumAuth(EnterUtil.encryption(hostNum));
        enterprise.setEnterDayAuth(EnterUtil.encryption(day));
        int date = DateUtil.timeStamp();
        enterprise.setEnterTimeAuth(EnterUtil.encryption(date + ""));
        enterprise.setState("0");
        enterprise.setCreationTime(new Date());
        enterprise.setEnterId(UUID.randomUUID().toString().replace("-", ""));
        Enterprise reEnter = enterRepository.save(enterprise);

        Users users = new Users();
        users.setEnterId(reEnter.getEnterId());
        users.setCreationTime(new Date());
        users.setUserName(userName);
        users.setType("1");
        users.setState("0");
        users.setName(reEnter.getEnterName());
        //加密
        String md5Str = DigestUtils.md5DigestAsHex(pow.getBytes());
        String encrypPow = DigestUtils.md5DigestAsHex(md5Str.getBytes());
        users.setPassword(encrypPow);
        users.setUserId(UUID.randomUUID().toString().replace("-", ""));

        Users re = usersRepository.save(users);
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("userId", re.getUserId());
        map.put("userName", re.getUserName());
        map.put("name", re.getName());
        map.put("pow", re.getPassword());
        map.put("type", re.getType());
        list.add(map);
        //更新保存默认账号id
        reEnter.setDefaultUserId(re.getUserId());
        enterRepository.saveAndFlush(reEnter);
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
            map.put("enterAuth", enter.getEnterAuth());
            map.put("hostNum", EnterUtil.decrypt(enter.getHostNumAuth()));
            String creatTime = EnterUtil.decrypt(enter.getEnterTimeAuth());
            String day = EnterUtil.decrypt(enter.getEnterDayAuth());
            if (creatTime != null && day != null) {
                String endTime = DateUtil.addOneday(creatTime, Integer.parseInt(day));
                map.put("enterTime", endTime);
            }
            Users users = usersRepository.findAllByUserId(enter.getDefaultUserId());
            map.put("userName", users.getUserName());
            map.put("userId", users.getUserId());
            map.put("creationTime", DateUtil.date2TimeStamp(enter.getCreationTime(), "yyyy-MM-dd HH:mm"));

            map.put("state", enter.getState() + "");
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
    public Enterprise modifyEnter(String enterId, String hostNum, String day, String userId) {
        //查询用户
        Users users = usersRepository.findAllByUserId(userId);
        if (users != null && !users.getType().equals("0")) {
            return null;
        }
        //查询企业信息
        Enterprise enterprise = enterRepository.findAllByEnterId(enterId);
        if (enterprise == null) {
            return null;
        }
        System.out.println(day);
        //企业信息
        String enterTime = EnterUtil.decrypt(enterprise.getEnterTimeAuth());
        //时间转时间
        long createTime = Long.parseLong(enterTime);
        long time = DateUtil.timeDateStamp(day, "yyyy-MM-dd");
        System.out.println(createTime);
        System.out.println(time);
        if (createTime > time) {
            enterprise.setEnterDayAuth(EnterUtil.encryption("0"));
        } else {
            int newDay = (int) (time - createTime) / (24 * 3600);
            if (newDay < 1) {
                enterprise.setEnterDayAuth(EnterUtil.encryption("1"));
            } else {
                enterprise.setEnterDayAuth(EnterUtil.encryption(newDay + ""));
            }
        }
        enterprise.setHostNumAuth(EnterUtil.encryption(hostNum));

        return enterRepository.saveAndFlush(enterprise);
    }

    @Override
    public Enterprise modifyEnterState(String enterId, String state) {

        Enterprise enterprise = enterRepository.findAllByEnterId(enterId);
        if (enterprise == null) {
            return null;
        }
        enterprise.setState(state);
        return enterRepository.saveAndFlush(enterprise);
    }

    @Override
    public String findEnterList(String state) {
        state = state == null || state.equals("") ? "" : state;
        List<Enterprise> enterprises = enterRepository.findAllByStateContaining(state);
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> mm = new HashMap<>();
        mm.put("enterId", "");
        mm.put("enterName", "所有企业");
        list.add(mm);
        for (Enterprise enter : enterprises) {
            Map<String, String> m = new HashMap<>();
            m.put("enterId", enter.getEnterId());
            m.put("enterName", enter.getEnterName());
            list.add(m);
        }
        return JSONObject.toJSONString(list);
    }

    @Override
    public boolean checkEnter(String enterId) {
        //查询企业
        Enterprise enterprise = enterRepository.findAllByEnterId(enterId);
        if (enterprise == null) {
            return false;
        } else {
            String creatTime = EnterUtil.decrypt(enterprise.getEnterTimeAuth());
            String day = EnterUtil.decrypt(enterprise.getEnterDayAuth());
            // System.out.println(creatTime + "#####" + day);
            if (creatTime != null && day != null) {
                String endTime = DateUtil.addOneday(creatTime, Integer.parseInt(day));
                long time = DateUtil.timeDateStamp(endTime, null);
                //System.out.println(endTime + "#######" + time);
                if (time != 0) {
                    if (time > DateUtil.timeStamp()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String registEnter(String no, String code) {
        //code解密后格式   {no:'',day:'',hostNum:''}
        //解密注册码
        String registCode = EnterUtil.decrypt(code);
        System.out.println("解析的数据" + registCode);
        if (registCode == null) {
            return null;
        }
        //json解析
        try {
            JSONObject jsonObject = JSONObject.parseObject(registCode);
            String regNo = jsonObject.getString("no");
            String regDay = jsonObject.getString("day");
            String regHostNum = jsonObject.getString("hostNum");
            if (!regNo.equals(no)) {
                return null;
            } else {
                System.out.println("进入账号");
                //根据id查询企业id
                Users users = usersService.findByUserName(no);
                if (users == null) {
                    return null;
                }
                System.out.println("进入查询企业");
                //根据企业id查询企业
                Enterprise enterprise = enterRepository.findAllByEnterIdAndDefaultUserId(users.getEnterId(), users.getUserId());
                if (enterprise == null) {
                    return null;
                }
                System.out.println("进入判断企业");
                if (enterprise.getState().equals("1")) {
                    return "-1";
                }
                System.out.println("写数据");
                enterprise.setEnterDayAuth(EnterUtil.encryption(regDay));
                enterprise.setHostNumAuth(EnterUtil.encryption(regHostNum));
                enterRepository.saveAndFlush(enterprise);
                return "ok";
            }
        } catch (Exception e) {

            return null;
        }
    }


}
