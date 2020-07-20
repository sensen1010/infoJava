package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.Program;

public interface ProgramService {

    //添加节目
    Program add(Program program);

    //根据名称查询节目
    String findByName(String name, String page, String size);

    //修改节目
    Program modifyProgram(Program program);


}
