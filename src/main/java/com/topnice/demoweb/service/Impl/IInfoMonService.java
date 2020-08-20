package com.topnice.demoweb.service.Impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.ClientUpdate;
import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.repository.EnterRepository;
import com.topnice.demoweb.repository.HostsRepository;
import com.topnice.demoweb.repository.UsersRepository;
import com.topnice.demoweb.service.ClientUpdateService;
import com.topnice.demoweb.service.InfoMonService;
import com.topnice.demoweb.util.AddressUtils;
import com.topnice.demoweb.util.FileUtil;
import com.topnice.demoweb.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;

@Service
@Transactional
public class IInfoMonService implements InfoMonService {

    @Value("${nomService.url}")
    public String service;
    @Value("${update.pathName}")
    public String updatePath;

    public String serviceUrl = "enter/enter";
    public String softUrl = "clientUpdate/newUpdate";

    @Autowired
    EnterRepository enterRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    HostsRepository hostsRepository;
    @Autowired
    ClientUpdateService clientUpdateService;

    @Override
    public boolean callService(String enterId) {
        Enterprise enterprise = enterRepository.findAllByEnterId(enterId);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.addIfAbsent("enterId", enterId);
        multiValueMap.addIfAbsent("innetIp", AddressUtils.getInnetIp() + "");
        multiValueMap.addIfAbsent("ipv4", AddressUtils.getV4IP() + "");
        if (enterprise == null) {
            multiValueMap.addIfAbsent("enterState", "-1");
            HttpClient.sendPostRequest(service + serviceUrl, multiValueMap);
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
        HttpClient.sendPostRequest(service + serviceUrl, multiValueMap);
        return true;
    }

    @Override
    public String updateApkService() {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.addIfAbsent("type", "APK");
        String re = HttpClient.sendPostRequest(service + softUrl, multiValueMap);
        if (re == null) {
            return null;
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = JSONObject.parseObject(re);
                if (jsonObject.getString("code").equals("1")) {
                    return null;
                } else {
                    JSONObject data = JSONObject.parseObject(jsonObject.getString("data"));
                    String softMd5 = data.getString("softMd5");
                    String softName = data.getString("softName");
                    String softClientId = data.getString("softId");
                    String softDownUrl = data.getString("downloadUrl");
                    String softContent = data.getString("softMd5");
                    String softSize = data.getString("softSize");
                    //下载文件
                    String fileUrl = "http://192.168.1.52:8080" + "/file/apk/" + softDownUrl;
                    try {
                        FileUtil.downLoadFromUrl(fileUrl, softDownUrl, FileUtil.getUpFileUrl("update"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //与数据库的MD5判断
                    ClientUpdate clientUpdate = clientUpdateService.LocalfindLastOne();
                    if (clientUpdate == null) {
                        //写入信息
                        ClientUpdate client = new ClientUpdate();
                        client.setApkMd5(softMd5);
                        client.setApkName(softName);
                        client.setClientId(softClientId);
                        client.setDownloadUrl(updatePath + "\\" + softDownUrl);
                        client.setModifyContent(softContent);
                        client.setApkSize(softSize);
                        client.setUpdateTime(new Date());
                        clientUpdateService.add(client);
                    } else {
                        String clientMd5 = clientUpdate.getApkMd5();
                        if (!clientMd5.equals(softMd5)) {
                            //写入信息
                            ClientUpdate client = new ClientUpdate();
                            client.setApkMd5(softMd5);
                            client.setClientId(softClientId);
                            client.setApkName(softName);
                            client.setDownloadUrl(updatePath + "\\" + softDownUrl);
                            client.setModifyContent(softContent);
                            client.setApkSize(softSize);
                            client.setUpdateTime(new Date());
                            clientUpdateService.add(client);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println(re);
        return re;
    }

}
