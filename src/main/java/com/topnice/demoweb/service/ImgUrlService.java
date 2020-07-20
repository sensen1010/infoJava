package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.ImgUrl;
import com.topnice.demoweb.exception.MyException;

public interface ImgUrlService {

    /**
     * @desc: 保存图片
     * @author: sen
     * @date:
     **/
    void saveImg(ImgUrl imgUrl) throws MyException;

    /**
     * @desc: 根据MD5查询图片
     * @author: sen
     * @date: 2020/6/17 0017 18:07
     **/
    ImgUrl findImgByImgMD5(String imgMd5);

    /**
     * @desc: 修改图片创建时间
     * @author: sen
     * @date: 2020/6/18 0018 8:50
     **/
    ImgUrl updateImgTime(ImgUrl imgUrl);

    /**
     * @desc: 根据图片名称、状态分页查询
     * @author: sen
     * @date: 2020/6/18 0018 9:24
     **/
    String findAllByImgNameStateShow(String imgName, String state, String showType, String page, String size);

    /**
     * @desc: 查询为视频、图片
     * @author: sen
     * @date: 2020/7/10 0010 15:14
     **/

    String findAllByIsImgMp4(String type, String page, String size);

    /**
     * @desc: 根据imgurlId查询
     * @author: sen
     * @date: 2020/6/18 0018 11:06
     **/
    ImgUrl findAllByImgUrlId(String imgUrlId);

}
