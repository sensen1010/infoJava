package com.topnice.demoweb.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@DynamicUpdate
@DynamicInsert
@Entity
@ApiModel(value = "信息发布布局", description = "发布布局")
public class InfoLayout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", hidden = true)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    private String uuid;

    private String name;

    private String imgUrl;

    private String layoutNum;

    private String type;// 0横布局 1竖布局

}
