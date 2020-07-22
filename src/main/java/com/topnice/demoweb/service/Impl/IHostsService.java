package com.topnice.demoweb.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Hosts;
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

    List<Map<String, Object>> lists;
    List<Map<String, String>> list;
    Map<String, Object> mapAll;
    /**
     * @desc: 添加主机
     * @author: sen
     * @date: 2020/6/19 0019 9:42
     **/
    @Override
    public Hosts add(String hostLinkId, String hostName, String enterId) {
        hostLinkId = hostLinkId == null || hostLinkId.equals("") ? "" : hostLinkId;
        Hosts rehost = hostsRepository.findAllByHostLinkId(hostLinkId);
        if (rehost == null) {
            Hosts hosts = new Hosts();
            hosts.setEnterId(enterId);
            hosts.setHostState("0");
            hosts.setHostName(hostName);
            hosts.setHostLinkId(hostLinkId);
            hosts.setCreationTime(new Date());
            hosts.setLinkState("0");
            hosts.setHostId(UUID.randomUUID().toString().replace("-", ""));
            return hostsRepository.save(hosts);
        }
        return null;
    }

    /**
     * @desc: 根据企业id查询主机
     * @author: sen
     * @date: 2020/6/19 0019 9:27
     **/
    @Override
    public String findHost(String enterId, String state, String page, String size) {
        state = state == null || state.equals("") ? "" : state;
        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);
        return JSONObject.toJSONString(lists);
    }

    /**
     * @desc: 超级管理员查询所有主机
     * @author: sen
     * @date: 2020/6/19 0019 9:29
     **/
    @Override
    public String findHosts(String hostName, String state, String linkState, String page, String size) {
        hostName = hostName == null || hostName.equals("") ? "" : hostName;
        state = state == null || state.equals("") ? "" : state;
        linkState = linkState == null || linkState.equals("") ? "" : linkState;
        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);
        //Pageable是接口，PageRequest是接口实现
        //PageRequest的对象构造函数有多个，page是页数，初始值是0，size是查询结果的条数，后两个参数参考Sort对象的构造方法
        // Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "id");旧方法 已弃用
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");
        Page<Hosts> all = hostsRepository.findAllByHostNameContainingAndHostStateContainingAndLinkStateContaining(hostName, state, linkState, pageable);

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
        System.out.println("进入查询界面：");

        return hostsRepository.findAllByHostLinkId(hostLinkId);
    }


    Map<String, String> hostsMap(Hosts hosts) {
        Map<String, String> map = new HashMap<>();
        map.put("id", hosts.getId() + "");
        map.put("hostId", hosts.getHostId());
        map.put("hostName", hosts.getHostName());
        map.put("hostLinkId", hosts.getHostLinkId());
        map.put("creationTime", DateUtil.date2TimeStamp(hosts.getCreationTime(), "yyyy-MM-dd HH:mm:ss") + "");
        map.put("linkTime", DateUtil.date2TimeStamp(hosts.getLinkTime(), "yyyy-MM-dd HH:mm:ss") + "");
        map.put("linkState", hosts.getLinkState() + "");
        return map;
    }

}
