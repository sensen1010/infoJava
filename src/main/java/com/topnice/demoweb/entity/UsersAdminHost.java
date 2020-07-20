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
@ApiModel(value = "用户安卓主机关联", description = "用户管理安卓机器")
public class UsersAdminHost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", hidden = true)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ApiModelProperty(value = "用户管理主机唯一id")
    private String usersHostId;

    @ApiModelProperty(value = "管理员表id")
    private String userId;

    @ApiModelProperty(value = "主机id")
    private String hostId;

    @ApiModelProperty(value = "管理状态 0为正常 1为禁止操作  2为删除")
    private String state;

    @ApiModelProperty(value = "添加时间")
    private Date creationTime;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

}
