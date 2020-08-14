package com.topnice.demoweb.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.entity.Hosts;
import com.topnice.demoweb.repository.EnterRepository;
import com.topnice.demoweb.repository.HostsRepository;
import com.topnice.demoweb.service.HostsService;
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
public class IHostsService implements HostsService {

    @Autowired
    private HostsRepository hostsRepository;

    @Autowired
    private EnterRepository enterRepository;


    List<Map<String, Object>> lists;
    List<Map<String, String>> list;
    Map<String, Object> mapAll;
    /**
     * @desc: 添加主机
     * @author: sen
     * @date: 2020/6/19 0019 9:42
     **/
    @Override
    public Hosts add(String hostIp,String hostLinkId, String hostName, String enterId) {
        hostLinkId = hostLinkId == null || hostLinkId.equals("") ? "" : hostLinkId;

            Hosts hosts = new Hosts();
            hosts.setEnterId(enterId);
            hosts.setHostState("0");
            hosts.setHostName(hostName);
            hosts.setHostIp(hostIp);
            hosts.setHostLinkId(hostLinkId);
            hosts.setCreationTime(new Date());
            hosts.setLinkState("0");
            hosts.setHostId(UUID.randomUUID().toString().replace("-", ""));
            return hostsRepository.save(hosts);
    }

    /**
     * @desc: 根据企业id查询主机
     * @author: sen
     * @date: 2020/6/19 0019 9:27
     **/
    @Override
    public String findHost(String enterId, String hostName,String linkState,String state, String page, String size) {
        state = state == null || state.equals("") ? "" : state;
        hostName = hostName == null || hostName.equals("") ? "" : hostName;
        linkState = linkState == null || linkState.equals("") ? "" : linkState;
        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);

        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");
        Page<Hosts> all = hostsRepository.findByEnterIdAndHostNameContainingAndLinkStateContaining(enterId,hostName,linkState,pageable);
        lists = new ArrayList<>();
        list = new ArrayList<>();
        mapAll = new HashMap<>();
        for (Hosts hosts : all) {
            list.add(hostsMap(hosts));
        }
        mapAll.put("data", JSONObject.toJSON(list));
        mapAll.put("size", all.getTotalElements());
        lists.add(mapAll);
        return JSONObject.toJSONString(lists);
    }

    /**
     * @desc: 超级管理员查询所有主机
     * @author: sen
     * @date: 2020/6/19 0019 9:29
     **/
    @Override
    public String findHosts(String enterId, String hostName, String linkState, String page, String size) {
        enterId = enterId == null || enterId.equals("") ? "" : enterId;
        hostName = hostName == null || hostName.equals("") ? "" : hostName;
        linkState = linkState == null || linkState.equals("") ? "" : linkState;
        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);
        //Pageable是接口，PageRequest是接口实现
        //PageRequest的对象构造函数有多个，page是页数，初始值是0，size是查询结果的条数，后两个参数参考Sort对象的构造方法
        // Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "id");旧方法 已弃用
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");
        Page<Hosts> all = hostsRepository.findAllByEnterIdContainingAndHostNameContainingAndLinkStateContaining(enterId, hostName, linkState, pageable);

        lists = new ArrayList<>();
        list = new ArrayList<>();
        mapAll = new HashMap<>();

        for (Hosts hosts : all) {
            list.add(hostsMap(hosts));
        }
        mapAll.put("data", JSONObject.toJSON(list));
        mapAll.put("size", all.getTotalElements());
        lists.add(mapAll);
        return JSONObject.toJSONString(lists);
    }

    @Override
    public Hosts modifyHostName(String hostId, String hostName) {
        hostId = hostId == null || hostId.equals("") ? "" : hostId;
        hostName = hostName == null || hostName.equals("") ? "" : hostName;
        Hosts hosts = hostsRepository.findAllByHostId(hostId);
        if (hostId == null || hostName.equals("")) {
            return null;
        }
        hosts.setHostName(hostName);
        return hostsRepository.saveAndFlush(hosts);
    }

    @Override
    public Hosts modifyHostState(String hostId, String state) {
        hostId = hostId == null || hostId.equals("") ? "" : hostId;
        state = state == null || state.equals("") ? "" : state;
        Hosts hosts = hostsRepository.findAllByHostId(hostId);
        if (hostId == null || state.equals("")) {
            return null;
        }
        hosts.setHostState(state);
        return hostsRepository.saveAndFlush(hosts);
    }

    @Override
    public Hosts modifyHostLState(Hosts hosts) {
        return hostsRepository.saveAndFlush(hosts);
    }

    @Override
    public Hosts findHostId(String hostLinkId) {
        return hostsRepository.findAllByHostLinkId(hostLinkId);
    }

    @Override
    public String findHostList(String enterId, String linkState, String state) {
        List<Hosts>  all=hostsRepository.findAllByEnterIdAndLinkStateAndHostState(enterId,linkState,state);
        System.out.println(enterId+all);
        list = new ArrayList<>();
        for (Hosts hosts : all) {
            list.add(hostsListMap(hosts));
        }
        return JSONObject.toJSONString(list);
    }

    @Override
    public Hosts findEnterIdAndHostLinkId(String enterId, String hostLinkId) {
        return hostsRepository.findAllByEnterIdAndHostLinkId(enterId, hostLinkId);
    }

    @Override
    public int findEnterHostNum(String enterId) {

        int num = hostsRepository.countAllByEnterId(enterId);

        return num;
    }

    Map<String, String> hostsListMap(Hosts hosts) {
        Map<String, String> map = new HashMap<>();
        map.put("hostId", hosts.getHostId());
        map.put("hostName", hosts.getHostName());
        return map;
    }
    Map<String, String> hostsMap(Hosts hosts) {
        Map<String, String> map = new HashMap<>();
        map.put("id", hosts.getId() + "");
        map.put("hostId", hosts.getHostId());
        map.put("hostName", hosts.getHostName());
        Enterprise enterprise = enterRepository.findAllByEnterId(hosts.getEnterId());
        map.put("enterName", enterprise.getEnterName());
        map.put("hostLinkId", hosts.getHostLinkId());
        map.put("creationTime", DateUtil.date2TimeStamp(hosts.getCreationTime(), "yyyy-MM-dd HH:mm:ss") + "");
        map.put("linkTime", DateUtil.date2TimeStamp(hosts.getLinkTime(), "yyyy-MM-dd HH:mm:ss") + "");
        map.put("linkState", hosts.getLinkState() + "");
        return map;
    }

}
