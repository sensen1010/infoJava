package com.topnice.demoweb.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@DynamicInsert
@DynamicUpdate
@Entity
@ApiModel(value = "安卓主机图片关联", description = "这是安卓主机图片关联")
public class HostsImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", hidden = true)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ApiModelProperty(value = "关联id")
    private String hostsImgId;

    @ApiModelProperty(value = "主机id")
    private String hostId;

    @ApiModelProperty(value = "图片地址id")
    private String imgUrlId;

    @ApiModelProperty(value = "主机图片状态 0为正常   1为禁用  2为删除")
    private String state;

    @ApiModelProperty(value = "添加时间")
    private Date creationTime;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;
}
