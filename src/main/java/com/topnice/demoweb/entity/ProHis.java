package com.topnice.demoweb.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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

    private String userId;//发布人id

    private String layoutId;//布局id

    private String content;//布局  {1:{},2{},3{},4{}}

    private String horseLamp;//0 不开启  1开启

    private String horseText;//跑马灯内容{1:{}，2:{}}


    private String type;//发布类型  0全部  1部分

    private String showType;//显示类型  0开机自动显示，1开机不显示

    private Date creationTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ProHisHost",
            joinColumns = {@JoinColumn(name = "proHisId", referencedColumnName = "proHisId")},
            inverseJoinColumns = {@JoinColumn(name = "proHisHostId", referencedColumnName = "proHisHostId")})
    private Set<ProHisHost> proHisHosts;

}
