package com.topnice.demoweb.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.InfoLayout;
import com.topnice.demoweb.entity.ProHis;
import com.topnice.demoweb.entity.ProHisHost;
import com.topnice.demoweb.entity.Program;
import com.topnice.demoweb.repository.*;
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

    @Autowired
    ProHisHostRepository hisHostRepository;
    @Autowired
    InfoLayoutRepository infoLayoutRepository;

    @Override
    public ProHis addProHis(ProHis proHis, String[] hostList) {
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
        if (proHis.getType().equals("1")) {
            for (String host : hostList) {
                ProHisHost proHisHost = new ProHisHost();
                proHisHost.setProHisHostId(UUID.randomUUID().toString().replace("-", ""));
                proHisHost.setHostId(host);
                proHisHost.setProHisId(reprohis.getProHisId());
                hisHostRepository.save(proHisHost);
            }
        }
        return reprohis;
    }

    @Override
    public String findAllByName(String name, String page, String size) {
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
            InfoLayout infoLayout = infoLayoutRepository.findAllByUuid(program.getLayoutId());
            map.put("layoutType", infoLayout.getType());
            map.put("layoutName", infoLayout.getName());
            map.put("layoutImgUrl", infoLayout.getImgUrl());
            map.put("layoutId", program.getLayoutId());
            map.put("horseLamp", program.getHorseLamp());
            map.put("horseText", program.getHorseText());
            map.put("proHisId", program.getProHisId());
            map.put("creationTime", DateUtil.date2TimeStamp(program.getCreationTime(), "yyyy-MM-dd HH:mm"));
            map.put("type", program.getType());//0 全部  1部分
            map.put("showType", program.getShowType());
            List<Map<String, String>> list1 = new ArrayList<>();
            for (ProHisHost proHisHost : program.getProHisHosts()) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("hostName", hostsRepository.findAllByHostId(proHisHost.getHostId()).getHostName());
                map1.put("hostId", proHisHost.getHostId());
                list1.add(map1);
            }
            map.put("hostList", JSONObject.toJSONString(list1));
            list.add(map);
        }
        maps.put("size", programs.getTotalElements());
        maps.put("data", JSONObject.toJSONString(list));
        lists.add(maps);
        return JSONObject.toJSONString(lists);
    }
}
