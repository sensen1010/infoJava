package com.topnice.demoweb.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;

public class FileUtil {

    @Value("${update.pathName}")
    private static String updatePathName;

    //    private static String UPLOAD_FOLDER = System.getProperty("user.dir");
//    private static String PATH = "\\webapps\\file\\";
//    private static String LocalUrl = "E:\\apache-tomcat-9.0.36\\webapps\\file\\";
    //tomcat地址
    private static String UPLOAD_FOLDER = System.getProperty("catalina.home");
    private static String PATH = "/webapps/file/";



    /**
     * @desc: 返回上传文件地址
     * @author: sen
     * @date: 2020/8/20 0020 10:12
     **/
    public static String getUpFileUrl(String pathName) {

        String upFileUrl = "";
        if (pathName == null || pathName.equals("")) {
            //upFileUrl = UPLOAD_FOLDER.substring(0, lastURL) + PATH;
            upFileUrl = UPLOAD_FOLDER + PATH;
        } else {
            //upFileUrl = UPLOAD_FOLDER.substring(0, lastURL) + PATH+pathName+"\\";
            upFileUrl = UPLOAD_FOLDER + PATH + pathName + "/";
        }
        return upFileUrl;
    }


    /**
     * @desc: 从互联网下载文件
     * @author: sen
     * @date: 2020/8/20 0020 10:15
     **/
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        System.out.println("info:" + url + " download success");
    }

    /**
     * @desc: 从输入流中获取字节数组
     * @author: sen
     * @date: 2020/8/20 0020 10:14
     **/
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * @desc: 获取文件的MD5
     * @author: sen
     * @date: 2020/8/20 0020 10:13
     **/
    public static String fileToBetyArray(InputStream fis) {
        MessageDigest md = null;
        //FileInputStream fis = null;
        byte[] buffer = null;
        try {
            md = MessageDigest.getInstance("MD5");
            // fis = new FileInputStream(file);
            buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            BigInteger bigInt = new BigInteger(1, md.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                buffer.clone();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @desc: 保存json文件
     * @author: sen
     * @date: 2020/8/20 0020 10:13
     **/
    private static void saveJsonToFile(String fileName, String data) {
        BufferedWriter writer = null;
        File file = new File(getUpFileUrl(updatePathName) + fileName + ".json");
        //如果文件不存在，则新建一个
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件写入成功！");
    }

    /**
     * @desc: 获取json文件
     * @author: sen
     * @date: 2020/8/20 0020 10:13
     **/
    public static String getJsonfromFile(String fileName) {
        String Path = getUpFileUrl(updatePathName) + fileName + ".json";
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }



    public static String fileMd5(InputStream inputStream) {
        try {
            return String.valueOf(DigestUtils.md5Digest(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @desc: 判断返回文件类型
     * @author: sen
     * @date: 2020/7/22 0022 10:24
     **/

    public static String fileType(String type) {
        String[] imgName = {"jpg", "png", "jpeg"};
        String[] wordName = {"xls", "xlsx", "doc", "docx", "ppt", "pptx"};
        String[] videoName = {"mp4", "flv", "avi"};
        String[] musicName = {"mp3"};
        type = type.toLowerCase();
        if (Arrays.asList(imgName).contains(type)) return "1";
        else if (Arrays.asList(wordName).contains(type)) return "2";
        else if (Arrays.asList(videoName).contains(type)) return "3";
        else if (Arrays.asList(musicName).contains(type)) return "4";
        return null;
    }

    public static String fileTypePath(String type) {
        switch (type) {
            case "1":
                return "img";
            case "2":
                return "word";
            case "3":
                return "video";
            case "4":
                return "music";
            default:
                return null;
        }
    }

    /**
     * @desc: 返回文件大小
     * @author: sen
     * @date: 2020/8/3 0003 13:38
     **/
    public static String  getSize(int size) {
        //获取到的size为：1705230
        int GB = 1024 * 1024 * 1024;//定义GB的计算常量
        int MB = 1024 * 1024;//定义MB的计算常量
        int KB = 1024;//定义KB的计算常量
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return  resultSize;
    }

    /**
     * @desc: 视频文件截图保存
     * @author: sen
     * @date: 2020/8/3 0003 13:38
     **/
    public static String getTempPath(String tempPath, String filePath) throws Exception {
//        String tempPath="    ";//保存的目标路径
        File targetFile = new File(tempPath);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        File file2 = new File(filePath);
        if (file2.exists()) {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file2);
            ff.start();
            int ftp = ff.getLengthInFrames();
            int flag = 0;
            Frame frame = null;
            while (flag <= ftp) {
                //获取帧
                frame = ff.grabImage();
                //过滤前5帧，避免出现全黑图片
                if ((flag > 40) && (frame != null)) {
                    break;
                }
                flag++;
            }
            ImageIO.write(FrameToBufferedImage(frame), "jpg", targetFile);
            ff.close();
            ff.stop();
        }
        return null;
    }

    private static RenderedImage FrameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }


}
