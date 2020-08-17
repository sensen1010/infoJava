package com.topnice.demoweb.service.Impl;

import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.repository.EnterRepository;
import com.topnice.demoweb.service.InfoMonService;
import com.topnice.demoweb.util.AddressUtils;
import com.topnice.demoweb.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;

@Service
@Transactional
public class IInfoMonService implements InfoMonService {

    public String url = "http://192.168.1.52:8084/mon/enter/enter";

    @Autowired
    EnterRepository enterRepository;

    @Override
    public boolean callService(String enterId) {
        Enterprise enterprise = enterRepository.findAllByEnterId(enterId);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.addIfAbsent("enterId", enterId);
        multiValueMap.addIfAbsent("innetIp", AddressUtils.getInnetIp() + "");
        multiValueMap.addIfAbsent("ipv4", AddressUtils.getV4IP() + "");
        if (enterprise == null) {
            multiValueMap.addIfAbsent("enterState", "-1");
            HttpClient.sendPostRequest(url, multiValueMap);
            return true;
        } else {
            multiValueMap.addIfAbsent("enterState", "0");
            multiValueMap.addIfAbsent("enterName", enterprise.getEnterName());
            multiValueMap.addIfAbsent("hostNum", enterprise.getHostNumAuth());
            multiValueMap.addIfAbsent("enterDay", enterprise.getEnterDayAuth());
            multiValueMap.addIfAbsent("enterAuth", enterprise.getEnterAuth());
            multiValueMap.addIfAbsent("signTime", enterprise.getEnterTimeAuth());
        }
        HttpClient.sendPostRequest(url, multiValueMap);
        return true;
    }
}
