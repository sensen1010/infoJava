package com.topnice.demoweb.config;

import java.io.File;

public class text {

    public static void main(String[] arge) {

        File file = new File("D:\\ce\\fileUp");
        deleteFileOrDirectory(file);

    }

    public static void deleteFileOrDirectory(File file) {
        if (null != file) {
            if (!file.exists()) {
                return;
            }

            int i;
            // file 是文件
            if (file.isFile()) {
                boolean result = file.delete();
                // 限制循环次数，避免死循环
                for (i = 0; !result && i++ < 10; result = file.delete()) {
                    // 垃圾回收
                    System.gc();
                }
                return;
            }

            // file 是目录
            File[] files = file.listFiles();
            if (null != files) {
                for (i = 0; i < files.length; ++i) {
                    deleteFileOrDirectory(files[i]);
                }
            }
            file.delete();
        }

    }


}
