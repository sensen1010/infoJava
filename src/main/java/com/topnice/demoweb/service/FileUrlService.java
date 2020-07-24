package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.FileUrl;
import com.topnice.demoweb.exception.MyException;

public interface FileUrlService {

    /**
     * @desc: 保存文件
     * @author: sen
     * @date:
     **/
    void add(FileUrl fileUrl) throws MyException;

    /**
     * @desc: 根据MD5查询文件
     * @author: sen
     * @date: 2020/6/17 0017 18:07
     **/
    FileUrl findByFileMD5(String fileMd5);

    /**
     * @desc: 修改图片创建时间
     * @author: sen
     * @date: 2020/6/18 0018 8:50
     **/
    FileUrl modifyFileTime(FileUrl fileUrl);

    /**
     * @desc: 管理员根据企业Id\图片名称、状态分页查询
     * @author: sen
     * @date: 2020/6/18 0018 9:24
     **/
    String findByFileNameAndStateAndType(String enterId, String fileName,String fileType ,String state, String page, String size);

    /**
     * @desc: 超级管理员根据企业Id\图片名称、状态分页查询
     * @author: sen
     * @date: 2020/6/18 0018 9:24
     **/
    String adminFindByFileNameAndStateAndType(String enterId, String fileName,String fileType,String state, String page, String size);



    /**
     * @desc: 根据企业id查询文件
     * @author: sen
     * @date: 2020/6/18 0018 9:24
     **/
    //String findByFileNameAndState( String state, String page, String size);

    /**
     * @desc: 查询为视频、图片
     * @author: sen
     * @date: 2020/7/10 0010 15:14
     **/

    String findByFileTypeId(String fileTypeId, String page, String size);

    /**
     * @desc: 根据fileUrlId查询
     * @author: sen
     * @date: 2020/6/18 0018 11:06
     **/
    FileUrl findByFileUrlId(String fileUrlId);

}
