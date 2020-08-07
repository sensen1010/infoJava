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
@ApiModel(value = "安卓主机", description = "这是安卓主机")
public class Hosts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", hidden = true)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @ApiModelProperty(value = "主机id")
    private String hostId;

    @ApiModelProperty(value = "企业Id")
    private String enterId;

    @ApiModelProperty(value = "主机名称")
    private String hostName;

    @ApiModelProperty(value = "主机Ip")
    private String hostIp;

    @ApiModelProperty(value = "主机websocket 链接id")
    private String hostLinkId;

    @ApiModelProperty(value = "主机websocket 状态 0为正常 1为禁用 2为删除")
    private String hostState;

    @ApiModelProperty(value = "主机websocket 链接状态 0为已链接 1为未链接")
    private String linkState;

    @ApiModelProperty(value = "最近链接时间")
    private Date linkTime;

    @ApiModelProperty(value = "添加时间")
    private Date creationTime;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

}
