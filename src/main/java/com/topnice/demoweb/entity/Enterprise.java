package com.topnice.demoweb.entity;

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
public class Enterprise implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", hidden = true)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @ApiModelProperty(value = "企业UUIDid")
    private String enterId;

    @ApiModelProperty(value = "企业名称")
    private String enterName;

    @ApiModelProperty(value = "默认账号id")
    private String defaultUserId;

    @ApiModelProperty(value = "企业认证信息")
    @Lob
    @Column(columnDefinition = "text")
    private String enterAuth;

    @ApiModelProperty(value = "主机数量认证信息")
    @Lob
    @Column(columnDefinition = "text")
    private String hostNumAuth;

    @ApiModelProperty(value = "企业可用天数")
    @Lob
    @Column(columnDefinition = "text")
    private String enterDayAuth;

    @ApiModelProperty(value = "企业创建时间")
    @Lob
    @Column(columnDefinition = "text")
    private String enterTimeAuth;

    @ApiModelProperty(value = "创建时间")
    private Date creationTime;

    @ApiModelProperty(value = "状态 0可用 1禁用")
    private String state;

    @ApiModelProperty(value = "企业ip")
    private String enterIp;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "enterUser",
            joinColumns = {@JoinColumn(name = "enterId", referencedColumnName = "enterId")},
            inverseJoinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")})
    private Set<Users> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "enterHosts",
            joinColumns = {@JoinColumn(name = "enterId", referencedColumnName = "enterId")},
            inverseJoinColumns = {@JoinColumn(name = "hostId", referencedColumnName = "hostId")})
    private Set<Hosts> hosts;

}
