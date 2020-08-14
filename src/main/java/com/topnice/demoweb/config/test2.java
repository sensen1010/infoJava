package com.topnice.demoweb.config;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class test2 {


    public static void main(String[] args) {

        // showView();
        String asd = "asdsadasdda,asdasd";
        try {
            JSONObject jsonObject = JSONObject.parseObject(asd);
        } catch (Exception e) {
            System.out.println("asd");
        }


    }

    void numasd() {
        int a = 1760;

    }

    public static void showView() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("mmssSSS");
        String date = simpleDateFormat.format(new Date());
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;

        System.out.println(date);
        System.out.println(System.currentTimeMillis());
        System.out.println(System.nanoTime());
    }

}
