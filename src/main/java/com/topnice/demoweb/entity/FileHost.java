package com.topnice.demoweb.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileHost {

    @Value("${prop.file-host}")
    private String FILE_HOST;

}
