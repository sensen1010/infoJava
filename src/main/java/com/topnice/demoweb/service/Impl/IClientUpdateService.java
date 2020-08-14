package com.topnice.demoweb.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.ClientUpdate;
import com.topnice.demoweb.repository.ClientUpdateRepository;
import com.topnice.demoweb.service.ClientUpdateService;
import com.topnice.demoweb.util.DateUtil;
import com.topnice.demoweb.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IClientUpdateService implements ClientUpdateService {

    @Autowired
    ClientUpdateRepository clientUpdateRepository;


    @Override
    public ClientUpdate add(ClientUpdate clientUpdate) {
        return clientUpdateRepository.save(clientUpdate);
    }

    @Override
    public String findALL(String page, String size) {

        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 10 : Integer.parseInt(size);
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "id");

        Page<ClientUpdate> clientUpdates = clientUpdateRepository.findAll(pageable);
        List<Map<String, Object>> lists = new ArrayList<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        for (ClientUpdate cli : clientUpdates) {
            Map<String, String> m = new HashMap<>();
            m.put("clientId", cli.getClientId());
            m.put("apkName", cli.getApkName());
            m.put("modifyContent", cli.getModifyContent());
            m.put("apkSize", FileUtil.getSize(Integer.parseInt(cli.getApkSize())));
            m.put("apkMd5", cli.getApkMd5());
            m.put("updateTime", DateUtil.date2TimeStamp(cli.getUpdateTime(), null));
            list.add(m);
        }
        maps.put("size", clientUpdates.getTotalElements());
        maps.put("data", JSONObject.toJSONString(list));
        lists.add(maps);
        return JSONObject.toJSONString(lists);
    }

    @Override
    public String findLastOne() {
        ClientUpdate clientUpdate = clientUpdateRepository.findLastOne();
        if (clientUpdate == null) {
            return null;
        }

        Map<String, String> m = new HashMap<>();
        m.put("clientId", clientUpdate.getClientId());
        m.put("apkName", clientUpdate.getApkName());
        m.put("modifyContent", clientUpdate.getModifyContent());
        m.put("downLoadUrl", clientUpdate.getDownloadUrl());
        m.put("apkSize", FileUtil.getSize(Integer.parseInt(clientUpdate.getApkSize())));
        m.put("apkMd5", clientUpdate.getApkMd5());
        m.put("updateTime", DateUtil.date2TimeStamp(clientUpdate.getUpdateTime(), null));
        return JSONObject.toJSONString(m);
    }
}
