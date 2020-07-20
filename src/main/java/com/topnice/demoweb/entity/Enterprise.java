package com.topnice.demoweb.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "企业", description = "企业")
public class Enterprise implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", hidden = true)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ApiModelProperty(value = "企业UUIDid")
    private String enterId;

    @ApiModelProperty(value = "企业名称")
    private String enterName;

    @ApiModelProperty(value = "主机数量")
    private String hostNum;

    @ApiModelProperty(value = "创建时间")
    private Date creationTime;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users",
            joinColumns = {@JoinColumn(name = "enterId", referencedColumnName = "enterId")},
            inverseJoinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")})
    private Set<ProHisHost> proHisHosts;

}
