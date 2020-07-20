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
@ApiModel(value = "图片地址", description = "这是安卓主机图片的地址")
public class ImgUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", hidden = true)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ApiModelProperty(value = "图片Id")
    private String imgUrlId;

    @ApiModelProperty(value = "图片名称")
    private String imgName;

    @ApiModelProperty(value = "图片链接地址")
    private String imgUrl;

    @ApiModelProperty(value = "图片Md5")
    private String imgMd5;

    @ApiModelProperty(value = "图片类型")
    private String imgType;

    @ApiModelProperty(value = "文件类型、0为图片视频 1为其他")
    private String fileType;

    @ApiModelProperty(value = "图片显示类型 0为在仓库 1为全部可见  2为部分可见")
    private String showType;

    @ApiModelProperty(value = "图片状态 0为正常   1为禁用  2为删除")
    private String state;

    @ApiModelProperty(value = "添加时间")
    private Date creationTime;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
