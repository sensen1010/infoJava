package com.topnice.demoweb.service;


import com.topnice.demoweb.entity.Program;

public interface ProgramService {

    //添加节目
    Program add(Program program);

    //修改节目
    Program modifyPro(String proId, String enterId, String content, String contentHtml);

    //企业根据节目名称查询节目
    String enterFindByName(String enterId, String name, String page, String size);

    //企业根据节目id查询节目
    String enterFindByProgramId(String enterId, String programId);

    //根据名称查询节目
    String findByName(String name, String page, String size);

    //修改节目
    Program modifyProgram(Program program);


}
