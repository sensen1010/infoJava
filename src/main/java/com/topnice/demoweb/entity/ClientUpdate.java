package com.topnice.demoweb.entity;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity
public class ClientUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    String clientId;

    String apkName;

    @Lob
    @Column(columnDefinition = "text")
    String modifyContent;

    String downloadUrl;

    String apkSize;

    String apkMd5;

    Date updateTime;

}
