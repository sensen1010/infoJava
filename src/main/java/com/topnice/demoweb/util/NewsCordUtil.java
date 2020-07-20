package com.topnice.demoweb.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class NewsCordUtil {

    @Value("${prop.file-host}")
    private String FILE_HOST;

    public static String PATTERN_L2DOMAIN = "\\w*\\.\\w*:";
    public static String PATTERN_IP = "(\\d*\\.){3}\\d*";
//    static NewsCordUtil newsCordUtil;
//
//   public static NewsCordUtil newsCordUtil(){
//        if (newsCordUtil!=null){
//            return newsCordUtil;
//        }else {
//            newsCordUtil=new NewsCordUtil();
//            return newsCordUtil;
//        }
//    }

    public String imgUrlUpdate(String newsBody) {
        Element doc = Jsoup.parseBodyFragment(newsBody).body();
        Elements pngs = doc.select("img[src]");
        for (Element element : pngs) {
            String url = element.attr("src");
            //判断是否为ip，是则更换图片
            Pattern ipPattern = Pattern.compile(PATTERN_IP);
            Matcher matcher = ipPattern.matcher(url);
            if (matcher.find()) {
                int inde = url.lastIndexOf("/");
                String imgUrl = url.substring(inde);
                element.attr("src", imgUrl);
            }
        }
        return newsBody = doc.toString();
    }

    public String documentBody(String newsBody) {
        Element doc = Jsoup.parseBodyFragment(newsBody).body();
        Elements pngs = doc.select("img[src]");
        String httpHost = FILE_HOST;
        for (Element element : pngs) {
            String imgUrl = element.attr("src");
            if (imgUrl.trim().startsWith("/")) { // 会去匹配我们富文本的图片的 src 的相对路径的首个字符，请注意一下
                imgUrl = httpHost + imgUrl;
                element.attr("src", imgUrl);
            }
        }
        String all = doc.toString();
        return all;
    }

}
