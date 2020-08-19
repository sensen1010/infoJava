package com.topnice.demoweb.service.Impl;

import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.repository.EnterRepository;
import com.topnice.demoweb.repository.HostsRepository;
import com.topnice.demoweb.repository.UsersRepository;
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

    public String serviceUrl = "http://192.168.1.52:8084/mon/enter/enter";
    public String softUrl = "http://192.168.1.52:8084/mon/clientUpdate/newUpdate";

    @Autowired
    EnterRepository enterRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    HostsRepository hostsRepository;

    @Override
    public boolean callService(String enterId) {
        Enterprise enterprise = enterRepository.findAllByEnterId(enterId);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.addIfAbsent("enterId", enterId);
        multiValueMap.addIfAbsent("innetIp", AddressUtils.getInnetIp() + "");
        multiValueMap.addIfAbsent("ipv4", AddressUtils.getV4IP() + "");
        if (enterprise == null) {
            multiValueMap.addIfAbsent("enterState", "-1");
            HttpClient.sendPostRequest(serviceUrl, multiValueMap);
            return true;
        } else {
            multiValueMap.addIfAbsent("enterState", "0");
            multiValueMap.addIfAbsent("enterName", enterprise.getEnterName());
            multiValueMap.addIfAbsent("hostNum", enterprise.getHostNumAuth());
            multiValueMap.addIfAbsent("enterDay", enterprise.getEnterDayAuth());
            multiValueMap.addIfAbsent("enterAuth", enterprise.getEnterAuth());
            multiValueMap.addIfAbsent("signTime", enterprise.getEnterTimeAuth());
            //根据默认用户id查询信息
            Users users = usersRepository.findAllByUserId(enterprise.getDefaultUserId());
            multiValueMap.addIfAbsent("defaultUser", users.getUserName());
            //主机数量
            int linkNum = hostsRepository.countAllByEnterId(enterId);
            multiValueMap.addIfAbsent("linkNum", linkNum + "");
        }
        HttpClient.sendPostRequest(serviceUrl, multiValueMap);
        return true;
    }

    @Override
    public String updateApkService() {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.addIfAbsent("type", "APK");
        String re = HttpClient.sendPostRequest(softUrl, multiValueMap);
        System.out.println(re);
        return re;
    }
}
