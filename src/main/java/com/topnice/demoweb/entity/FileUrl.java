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
@ApiModel(value = "文件地址", description = "这是文件的地址")
public class FileUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", hidden = true)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ApiModelProperty(value = "文件Id")
    private String fileUrlId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件链接地址")
    private String fileUrl;

    @ApiModelProperty(value = "文件Md5")
    private String fileMd5;

    @ApiModelProperty(value = "图片类型")
    private String imgType;

    @ApiModelProperty(value = "文件类型Id")
    private String fileTypeId;

    @ApiModelProperty(value = "企业Id")
    private String enterId;

    @ApiModelProperty(value = "图片状态 0为正常   1为禁用  2为删除")
    private String state;

    @ApiModelProperty(value = "添加时间")
    private Date creationTime;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
