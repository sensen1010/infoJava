package com.topnice.demoweb.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.ProHis;
import com.topnice.demoweb.entity.Program;
import com.topnice.demoweb.repository.HostsRepository;
import com.topnice.demoweb.repository.ProHisRepository;
import com.topnice.demoweb.repository.ProgramRepository;
import com.topnice.demoweb.service.ProHisService;
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
public class IProHisService implements ProHisService {

    @Autowired
    ProHisRepository proHisRepository;

    @Autowired
    ProgramRepository programRepository;

    @Autowired
    HostsRepository hostsRepository;


    @Override
    public ProHis add(ProHis proHis) {
        proHis.setProHisId(UUID.randomUUID().toString().replace("-", ""));
        proHis.setCreationTime(new Date());
        //根据节目id查询节目信息
        Program program = programRepository.findAllByProId(proHis.getProId());
        System.out.println(program);
        if (program == null) {
            return null;
        }
        //设置信息
        proHis.setName(program.getName());
        proHis.setUserId("");
        proHis.setLayoutId(program.getLayoutId());
        proHis.setContent(program.getContent());
        proHis.setHorseLamp(program.getHorseLamp());
        proHis.setHorseText(program.getHorseText());

        ProHis reprohis = proHisRepository.save(proHis);
        return reprohis;
    }

    @Override
    public String findByName(String name, String page, String size) {
        name = name == null || name.equals("") ? "" : name;

        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");
        Page<ProHis> programs = proHisRepository.findAllByNameContaining(name, pageable);
        List<Map<String, Object>> lists = new ArrayList<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();

        for (ProHis program : programs) {
            Map<String, String> map = new HashMap<>();
            map.put("id", program.getId() + "");
            map.put("name", program.getName());
            map.put("content", program.getContent());
            map.put("layoutId", program.getLayoutId());
            map.put("horseLamp", program.getHorseLamp());
            map.put("horseText", program.getHorseText());
            map.put("proHisId", program.getProHisId());
            map.put("creationTime", DateUtil.date2TimeStamp(program.getCreationTime(), "yyyy-MM-dd HH:mm"));
            map.put("type", program.getType());//0 全部  1部分
            map.put("showType", program.getShowType());
            list.add(map);
        }
        maps.put("size", programs.getTotalElements());
        maps.put("data", JSONObject.toJSONString(list));
        lists.add(maps);
        return JSONObject.toJSONString(lists);
    }
}
