package com.topnice.demoweb.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.*;
import com.topnice.demoweb.repository.HostsRepository;
import com.topnice.demoweb.repository.ProHisHRepository;
import com.topnice.demoweb.repository.ProHisRepository;
import com.topnice.demoweb.repository.ProgramRepository;
import com.topnice.demoweb.service.EnterpriseService;
import com.topnice.demoweb.service.ProHisService;
import com.topnice.demoweb.service.UsersService;
import com.topnice.demoweb.service.WebSocketServer;
import com.topnice.demoweb.util.DateUtil;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
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
    EnterpriseService enterpriseService;

    @Autowired
    UsersService usersService;

    @Autowired
    ProHisHRepository proHisHRepository;




    @Override
    public ProHis add(String enterId,String userId,String proId,String type,String[] hostList) {

        //根据企业id查询企业
        Enterprise enterprise=enterpriseService.findByEnterId(enterId);
        if (enterprise == null) {
            return null;
        }
        //根据管理员id查询信息
//        Users users=usersService.findByUserId(userId);
        //根据节目id查询节目信息
        Program program = programRepository.findAllByProId(proId);
        if (program == null) {
            return null;
        }
        //保存信息
        ProHis proHis=new ProHis();
        proHis.setType(type);
        proHis.setProHisId(UUID.randomUUID().toString().replace("-",""));
        proHis.setProId(program.getProId());
        proHis.setName(program.getName());
        proHis.setEnterId(enterId);
        proHis.setUserId(userId);
        proHis.setLayoutType(program.getLayoutType());
        proHis.setContent(program.getContent());
        proHis.setContentHtml(program.getContentHtml());
        proHis.setCreationTime(new Date());
        ProHis reprohis = proHisRepository.save(proHis);
        //0全部  1部分
        if (type.equals("0")){
            //向该企业下所有主机发送消息
            try {
                WebSocketServer.senEnterAllMessage(enterId,proId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //向该企业下部分主机发送消息
            try {
                for (int a=0;hostList.length>a;a++){
                    ProHisH proHisH=new ProHisH();
                    proHisH.setProHisHId(UUID.randomUUID().toString().replace("-",""));
                    proHisH.setHostId(hostList[a]);
                    proHisH.setProHisId(reprohis.getProHisId());
                    proHisHRepository.save(proHisH);
                    //根据id查询连接id
                     Hosts hosts= hostsRepository.findAllByHostId(hostList[a]);
                     if (hosts!=null){
                         WebSocketServer.sendInfo(proId,enterId,hosts.getHostLinkId());
                     }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
//            map.put("name", program.getName());
//            map.put("content", program.getContent());
//            map.put("proHisId", program.getProHisId());
//            map.put("creationTime", DateUtil.date2TimeStamp(program.getCreationTime(), "yyyy-MM-dd HH:mm"));
//            map.put("type", program.getType());//0 全部  1部分
//            map.put("showType", program.getShowType());
            list.add(map);
        }
        maps.put("size", programs.getTotalElements());
        maps.put("data", JSONObject.toJSONString(list));
        lists.add(maps);
        return JSONObject.toJSONString(lists);
    }

    @Override
    public String enterFindByName(String enterId, String name, String page, String size) {
              name = name == null || name.equals("") ? "" : name;
            //如果为null默认为0
            Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
            //如果为null默认为10
            Integer rsize = size == null || size.equals("") ? 5 : Integer.parseInt(size);
            Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");
            Page<ProHis> programs = proHisRepository.findAllByEnterIdAndNameContaining(enterId,name,pageable);
            List<Map<String, Object>> lists = new ArrayList<>();
            List<Map<String, String>> list = new ArrayList<>();
            Map<String, Object> maps = new HashMap<>();
            for (ProHis program : programs) {
                Map<String, String> map = new HashMap<>();
                map.put("id", program.getId() + "");
                map.put("name", program.getName());
                map.put("content", program.getContent());
                map.put("contentHtml", program.getContentHtml());
                map.put("layoutType", program.getLayoutType());
                map.put("proHisId", program.getProHisId());
                map.put("creationTime", DateUtil.date2TimeStamp(program.getCreationTime(), "yyyy-MM-dd HH:mm"));
                List<ProHisH> proHisHES=proHisHRepository.findAllByProHisId(program.getProHisId());
               if (proHisHES!=null){
                   List<Map<String,String>> proList=new ArrayList<>();
                   for (ProHisH proHisH:proHisHES){
                       Hosts hosts=hostsRepository.findAllByHostId(proHisH.getHostId());
                       Map<String, String> proMap= new HashMap<>();
                       proMap.put("hostId",hosts.getHostId()+ "");
                       proMap.put("name",hosts.getHostName()+ "");
                       proList.add(proMap);
                   }
                   map.put("hostList",JSONObject.toJSONString(proList));
                }
                Users users=usersService.findByUserId(program.getUserId());
                map.put("userName",users.getName()+"("+users.getUserName()+")");
                map.put("type", program.getType());//0 全部  1部分
                list.add(map);
        }
        maps.put("size", programs.getTotalElements());
        maps.put("data", JSONObject.toJSONString(list));
        lists.add(maps);
        return JSONObject.toJSONString(lists);
    }
}
