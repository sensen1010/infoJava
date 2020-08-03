package com.topnice.demoweb.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@DynamicInsert
@DynamicUpdate
@Entity
@ApiModel(value = "节目历史表", description = "历史表")
public class ProHis implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    private String proHisId;

    private String proId;//节目id

    private String name;//节目名称

    private String enterId;//企业id

    private String userId;//发布人id

    private String layoutType;//布局类型

    private String content;//布局内容

    private String type;//发布类型  0全部  1部分

    private String showType;//显示类型  0开机自动显示，1开机不显示

    private String startTime;//开启时间、结束时间

    private String week;//星期

    private String weight;//权重

    private Date creationTime;

}
