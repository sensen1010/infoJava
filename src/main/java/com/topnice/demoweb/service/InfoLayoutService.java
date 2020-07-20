package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.InfoLayout;

public interface InfoLayoutService {

    String findAll(String type);

    InfoLayout addLayout(InfoLayout infoLayout);

}
