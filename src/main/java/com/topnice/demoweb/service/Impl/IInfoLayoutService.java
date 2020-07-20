package com.topnice.demoweb.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.InfoLayout;
import com.topnice.demoweb.repository.InfoLayoutRepository;
import com.topnice.demoweb.service.InfoLayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class IInfoLayoutService implements InfoLayoutService {

    @Autowired
    InfoLayoutRepository infoLayoutRepository;

    @Override
    public String findAll(String type) {

        type = type == null || type.equals("") ? "0" : type;

        List<InfoLayout> all = infoLayoutRepository.findAllByType(type);

        if (all != null) {
            List<Map<String, String>> list = new ArrayList<>();
            for (InfoLayout infoLayout : all) {
                Map<String, String> map = new HashMap<>();
                map.put("uuid", infoLayout.getUuid());
                map.put("imgUrl", infoLayout.getImgUrl());
                map.put("layoutNum", infoLayout.getLayoutNum());
                map.put("name", infoLayout.getName());
                map.put("type", infoLayout.getType());
                list.add(map);
            }
            return JSONObject.toJSONString(list);
        }
        return null;
    }

    @Override
    public InfoLayout addLayout(InfoLayout infoLayout) {
        infoLayout.setUuid(UUID.randomUUID().toString().replace("-", ""));
        return infoLayoutRepository.save(infoLayout);
    }
}
