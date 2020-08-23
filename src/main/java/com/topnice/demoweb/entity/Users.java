package com.topnice.demoweb.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "用户", description = "这是用户对象")
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", hidden = true)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @ApiModelProperty(value = "用户UUIDid")
    private String userId;

    @ApiModelProperty(value = "企业id")
    private String enterId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "密码")
    private String pow;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;//0为超级管理员 、 1为企业超级管理员 、2为企业普通用户

    @ApiModelProperty(value = "创建时间")
    private Date creationTime;


}
