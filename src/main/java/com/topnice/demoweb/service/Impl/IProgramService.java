package com.topnice.demoweb.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.InfoLayout;
import com.topnice.demoweb.entity.Program;
import com.topnice.demoweb.repository.InfoLayoutRepository;
import com.topnice.demoweb.repository.ProgramRepository;
import com.topnice.demoweb.service.ProgramService;
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
public class IProgramService implements ProgramService {


    @Autowired
    ProgramRepository programRepository;

    @Autowired
    InfoLayoutRepository infoLayoutRepository;


    @Override
    public Program addProgram(Program program) {

        if (program.getHorseLamp().equals("1")) {
            if (program.getHorseText() != null && !program.equals("")) {
                String a = program.getHorseText().replace("[", "").replace("]", "");
                program.setHorseText(a);
            }
        }
        program.setCreationTime(new Date());
        program.setProId(UUID.randomUUID().toString().replace("-", ""));
        return programRepository.save(program);
    }

    @Override
    public String findAllByName(String name, String page, String size) {

        name = name == null || name.equals("") ? "" : name;

        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");

        Page<Program> programs = programRepository.findAllByNameContaining(name, pageable);
        List<Map<String, Object>> lists = new ArrayList<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        for (Program program : programs) {
            Map<String, String> map = new HashMap<>();
            map.put("id", program.getId() + "");
            map.put("proId", program.getProId());
            map.put("name", program.getName());
            map.put("content", program.getContent());
            map.put("horseText", program.getHorseText());
            map.put("horseLamp", program.getHorseLamp());
            InfoLayout infoLayout = infoLayoutRepository.findAllByUuid(program.getLayoutId());
            map.put("layoutName", infoLayout.getName());
            map.put("layoutType", infoLayout.getType());
            map.put("layoutImgUrl", infoLayout.getImgUrl());
            map.put("layoutId", program.getLayoutId());
            map.put("userId", program.getUserId());
            // map.put("set",program.getProHis()+"");
            map.put("creationTime", DateUtil.date2TimeStamp(program.getCreationTime(), "yyyy-MM-dd HH:mm"));
            list.add(map);
        }
        maps.put("size", programs.getTotalElements());
        maps.put("data", JSONObject.toJSONString(list));
        lists.add(maps);
        return JSONObject.toJSONString(lists);
    }

    @Override
    public Program updateProgram(Program program) {
        return null;
    }
}
