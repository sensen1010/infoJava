package com.topnice.demoweb.entity;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert

@Entity
@ApiModel(value = "节目表", description = "节目表")
public class Program implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    private String proId;

    private String name;//名称

    private String enterId;//企业id

    private String userId;//发布人id

    private String layoutType;//布局类型

    private String state;

    @Lob
    @Column(columnDefinition = "text")
    private String content;//布局内容  {1:{},2{},3{},4{}}

    @Lob
    @Column(columnDefinition = "text")
    private String contentHtml;//布局html代码，用于预览

    private Date creationTime;



}
