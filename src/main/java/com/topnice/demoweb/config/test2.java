package com.topnice.demoweb.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class test2 {


    public static void main(String[] args) {

        showView();

    }

    void numasd() {
        int a = 1760;

    }

    public static void showView() {
        String date = "[{\"id\":\"1\",\"type\":\"bj\",\"resi\":false,\"drag\":false,\"w\":960,\"h\":540,\"color\":\"\",\"barg\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595913207485&di=99086d5c5dc78689004212475b81a84e&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F4%2F56f62bab021a8.jpg\",\"z\":1},{\"id\":\"2\",\"type\":\"img\",\"x\":200,\"y\":50,\"style\":\"\",\"resi\":true,\"drag\":true,\"w\":100,\"h\":100,\"z\":3},{\"id\":\"3\",\"type\":\"video\",\"x\":20,\"y\":50,\"resi\":true,\"drag\":true,\"w\":100,\"h\":100,\"z\":2},{\"id\":\"4\",\"type\":\"text\",\"x\":200,\"y\":200,\"w\":100,\"resi\":true,\"drag\":true,\"h\":100,\"z\":2,\"color\":\"#FF1493\",\"textAlign\":\"center\",\"size\":\"16px\"},{\"id\":5,\"type\":\"text\",\"x\":0,\"y\":0,\"w\":150,\"h\":100,\"z\":5,\"resi\":true,\"drag\":true,\"color\":\"#FF1493\",\"textAlign\":\"center\",\"size\":\"16px\"}]";
        JSONArray jsonArray = JSONArray.parseArray(date);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

            System.out.println(jsonObject2.getString("x"));
        }
    }

}
