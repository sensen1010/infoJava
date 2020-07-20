package com.topnice.demoweb.entity;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Data
@DynamicUpdate
@DynamicInsert
@Entity
public class ProHisHost implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    private String proHisHostId;

    private String proHisId; //历史表id

    private String hostId;//主机id


}
