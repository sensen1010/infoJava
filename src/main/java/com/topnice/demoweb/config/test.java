package com.topnice.demoweb.config;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class test {
    private static String getLocalMac() {

        InetAddress ia = null;
        StringBuffer sb = new StringBuffer("");

        try {

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface != null) {

                    String name = networkInterface.getName();

                    if (!name.equals("en0")) {
                        continue;
                    }

                    byte[] bytes = networkInterface.getHardwareAddress();
                    if (bytes != null) {
                        for (int i = 0; i < bytes.length; i++) {
                            if (i != 0) {
                                sb.append(":");
                            }
                            int tmp = bytes[i] & 0xff; // 字节转换为整数
                            String str = Integer.toHexString(tmp);
                            if (str.length() == 1) {
                                sb.append("0" + str);
                            } else {
                                sb.append(str);
                            }
                        }
                    }
                }
            }

            System.out.println("本机MAC地址:" + sb.toString().toLowerCase());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString().toLowerCase();

    }

    public static void main(String[] args) {

        //getLocalMac();
//        String[] imgName = {"jpg", "png", "mp4", "jpeg"};
//        String data="mP4";
////        data=data.m,ge.
//        data=data.toLowerCase();
//        boolean flag = Arrays.asList(imgName).contains(data);
//        String filePath = "D:\\ce\\asd\\mp4";
//        File targetFile = new File(filePath);
//        if (!targetFile.exists()) {
//            targetFile.mkdirs();
//        }
//        System.out.println(flag);


        String upFileName = "asda.asdas.dasd.mp4";

        int lastFile = upFileName.lastIndexOf(".");
        System.out.println(lastFile);
        String upFileType = upFileName.substring(lastFile + 1, upFileName.length());
        System.out.println(upFileType);

    }

}
