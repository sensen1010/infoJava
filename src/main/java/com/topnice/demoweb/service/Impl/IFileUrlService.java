package com.topnice.demoweb.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.entity.FileUrl;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.exception.MyException;
import com.topnice.demoweb.repository.FileUrlRepository;
import com.topnice.demoweb.service.EnterpriseService;
import com.topnice.demoweb.service.FileUrlService;
import com.topnice.demoweb.service.UsersService;
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
public class IFileUrlService implements FileUrlService {

    @Autowired
    FileUrlRepository fileUrlRepository;

    @Autowired
    EnterpriseService enterpriseService;

    @Autowired
    UsersService usersService;


    @Override
    public void add(FileUrl fileUrl) throws MyException {
        fileUrlRepository.save(fileUrl);
    }

    @Override
    public FileUrl findByFileMD5(String fileMd5) {
        return fileUrlRepository.findAllByFileMd5(fileMd5);
    }

    @Override
    public FileUrl modifyFileTime(FileUrl fileUrl) {
        return fileUrlRepository.saveAndFlush(fileUrl);
    }


    @Override
    public String findByFileNameAndStateAndType(String enterId, String fileName, String fileTypeId, String state, String page, String size) {
        fileName = fileName == null || fileName.equals("") ? "" : fileName;
        fileTypeId = fileTypeId == null || fileTypeId.equals("") ? "" : fileTypeId;
        state = state == null || state.equals("") ? "0" : state;

        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 32 : Integer.parseInt(size);
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "updateTime");

        Page<FileUrl> reFileUrl = fileUrlRepository.findAllByEnterIdAndFileNameContainingAndFileTypeIdContainingAndStateContaining(enterId, fileName, fileTypeId, state, pageable);
        List<Map<String, Object>> lists = new ArrayList<>();
        Map<String, Object> m1 = new HashMap<>();
        m1.put("data", allFile(reFileUrl));
        m1.put("size", reFileUrl.getTotalElements());
        lists.add(m1);
        return JSONObject.toJSONString(lists);
    }

    @Override
    public String adminFindByFileNameAndStateAndType(String enterId, String fileName, String fileTypeId, String state, String page, String size) {
        enterId = enterId == null || enterId.equals("") ? "" : enterId;
        fileName = fileName == null || fileName.equals("") ? "" : fileName;
        fileTypeId = fileTypeId == null || fileTypeId.equals("") ? "" : fileTypeId;
        state = state == null || state.equals("") ? "0" : state;
        //如果为null默认为0
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 32 : Integer.parseInt(size);
        //Pageable是接口，PageRequest是接口实现
        //PageRequest的对象构造函数有多个，page是页数，初始值是0，size是查询结果的条数，后两个参数参考Sort对象的构造方法
        // Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "id");旧方法 已弃用
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "updateTime");

        Page<FileUrl> reFileUrl = fileUrlRepository.findAllByEnterIdContainingAndFileNameContainingAndFileTypeIdContainingAndStateContaining(enterId, fileName, fileTypeId, state, pageable);

        List<Map<String, Object>> lists = new ArrayList<>();
        Map<String, Object> m1 = new HashMap<>();
        m1.put("data",allFile(reFileUrl));
        m1.put("size", reFileUrl.getTotalElements());
        lists.add(m1);
        return JSONObject.toJSONString(lists);
    }

    String  allFile( Page<FileUrl> all){
        List<Map<String, String>> lists = new ArrayList<>();
        try {

            for (FileUrl fileUrl : all) {
                Map<String, String> m = new HashMap<>();
                m.put("id", fileUrl.getId() + "");
                m.put("fileName", fileUrl.getFileName() + "");
                m.put("fileUrlId", fileUrl.getFileUrlId() + "");
                m.put("fileType", fileUrl.getFileType() + "");
                m.put("fileTypeId", fileUrl.getFileTypeId() + "");
                Enterprise enterprise = enterpriseService.findByEnterId(fileUrl.getEnterId());
                m.put("enterName", enterprise.getEnterName() + "");
                Users users = usersService.findByUserId(fileUrl.getUserId());
                m.put("userName", users.getUserName());
                if (fileUrl.getFileSize() != null) {
                    m.put("fileSize", FileUtil.getSize(Integer.parseInt(fileUrl.getFileSize())));
                }
                if (fileUrl.getFileTypeId().equals("3")) {
                    m.put("videoImg", fileUrl.getVideoImg() + "");
                }
                m.put("fileUrl", fileUrl.getFileUrl() + "");
                m.put("fileMd5", fileUrl.getFileMd5() + "");
                m.put("state", fileUrl.getState() + "");
                m.put("creationTime", DateUtil.date2TimeStamp(fileUrl.getCreationTime(), "yyyy-MM-dd HH:mm:ss") + "");
                m.put("deleteTime", DateUtil.date2TimeStamp(fileUrl.getDeleteTime(), "yyyy-MM-dd HH:mm:ss") + "");
                m.put("updateTime", DateUtil.date2TimeStamp(fileUrl.getUpdateTime(), "yyyy-MM-dd HH:mm:ss") + "");
                lists.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JSONObject.toJSONString(lists);
    }


    @Override
    public String findByFileTypeId(String fileTypeId, String page, String size) {
        if (fileTypeId == null || fileTypeId.equals("")) {
            return null;
        }
        Integer rpage = page == null || page.equals("") ? 0 : Integer.parseInt(page);
        //如果为null默认为10
        Integer rsize = size == null || size.equals("") ? 32 : Integer.parseInt(size);
        //Pageable是接口，PageRequest是接口实现
        //PageRequest的对象构造函数有多个，page是页数，初始值是0，size是查询结果的条数，后两个参数参考Sort对象的构造方法
        // Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "id");旧方法 已弃用
        Pageable pageable = PageRequest.of(rpage, rsize, Sort.Direction.DESC, "updateTime");

        Page<FileUrl> reFileUrl = fileUrlRepository.findAllByFileTypeId(fileTypeId, pageable);
        List<Map<String, Object>> lists = new ArrayList<>();
        Map<String, Object> m1 = new HashMap<>();
        List<Map<String, String>> lists2 = new ArrayList<>();
        for (FileUrl fileUrl : reFileUrl) {
            Map<String, String> m = new HashMap<>();
            m.put("id", fileUrl.getId() + "");
            m.put("fileName", fileUrl.getFileName() + "");
            m.put("fileUrl", fileUrl.getFileUrl() + "");
            m.put("fileType", fileUrl.getFileType() + "");
            lists2.add(m);
        }
        m1.put("data", JSONObject.toJSONString(lists2));
        m1.put("size", reFileUrl.getTotalElements());
        lists.add(m1);

        return JSONObject.toJSONString(lists);
    }

    @Override
    public FileUrl findByFileUrlId(String fileUrlId) {
        return fileUrlRepository.findAllByFileUrlId(fileUrlId);
    }
}
