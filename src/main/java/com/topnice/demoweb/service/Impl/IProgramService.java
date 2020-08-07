package com.topnice.demoweb.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Program;
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



    @Override
    public Program add(Program program) {
        program.setCreationTime(new Date());
        program.setProId(UUID.randomUUID().toString().replace("-", ""));
        return programRepository.save(program);
    }

    @Override
    public Program modifyPro(String proId, String enterId, String content, String contentHtml) {

        //查询企业id+节目id是否存在
        Program program = programRepository.findAllByProIdAndEnterId(proId, enterId);

        if (program == null) {
            return null;
        }
        program.setContent(content);
        program.setContentHtml(contentHtml);
        programRepository.saveAndFlush(program);
        return program;
    }

    @Override
    public String enterFindByName(String enterId, String name, String page, String size) {

        name = name == null || name.equals("") ? "" : name;

        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 5 : Integer.parseInt(size);
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");

        Page<Program> programs = programRepository.findAllByEnterIdAndNameContaining(enterId, name, pageable);
        List<Map<String, Object>> lists = new ArrayList<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        for (Program program : programs) {
            Map<String, String> map = new HashMap<>();
            map.put("id", program.getId() + "");
            map.put("proId", program.getProId());
            map.put("name", program.getName());
            map.put("content", program.getContent());
            map.put("layoutType", program.getLayoutType());
            map.put("userId", program.getUserId());
            map.put("contentHtml", program.getContentHtml() + "");
            map.put("creationTime", DateUtil.date2TimeStamp(program.getCreationTime(), "yyyy-MM-dd HH:mm"));
            list.add(map);
        }
        maps.put("size", programs.getTotalElements());
        maps.put("data", JSONObject.toJSONString(list));
        lists.add(maps);
        return JSONObject.toJSONString(lists);
    }

    @Override
    public String enterFindByProgramId(String enterId, String programId) {
        Program program = programRepository.findAllByProIdAndEnterId(programId, enterId);
        if (program == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", program.getId() + "");
        map.put("proId", program.getProId());
        map.put("name", program.getName());
        map.put("content", program.getContent());
        return JSONObject.toJSONString(map);
    }

    @Override
    public String findByName(String name, String page, String size) {

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
    public Program modifyProgram(Program program) {
        return null;
    }
}
