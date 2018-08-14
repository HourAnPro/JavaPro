package com.houran.reptile.parse.pengpai;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.houran.reptile.model.jd.JdModel;
import com.houran.reptile.model.pengpai.PengPaiModel;


/***
 * 利用jsoup抓取网页图片:https://www.cnblogs.com/zyoung/p/6579963.html
 * 
 * Jsoup通过URL获取文档，获取href属性内容然后能过IO流读取文件即可：https://blog.csdn.net/qq_37521135/article/details/78783021
 * @author HourAn
 *
 */
public class PengPaiParse {
	
	public static List<PengPaiModel> getData (String html) throws Exception{
        //获取的数据，存放在集合中
        List<PengPaiModel> data = new ArrayList<PengPaiModel>();
        //采用Jsoup解析
        Document doc = Jsoup.parse(html);
        //获取html标签中的内容
        Elements elements=doc.select("div[id=listContent]").select("div[class=news_li]");
        //elements.remove(elements.size()-1);
        for (Element ele:elements) {
            String ppID=ele.attr("id");
            //得到图片url
            Elements  imgElements =ele.select("div[class=news_tu]").select("a[class=tiptitleImg]").select("img");
            String fielPath="D:/imges";
          //创建一个对象，使用Model直接进行封装
            PengPaiModel ppModel=new PengPaiModel();            
            for(Element imgEle:imgElements){
            	String imgUrl=imgEle.attr("src");
            	imgUrl="https:"+imgUrl;
            	readAndDownloadImgByte(fielPath,imgUrl);
            	ppModel.setImgUrl(imgUrl);
            }
           
            
            String title=ele.select("h2").select("a").text();
            String content=ele.select("p").text();
            
            //对象的值
            ppModel.setId(ppID);
            ppModel.setTitle(title);
            ppModel.setContent(content);
     
            //将每一个对象的值，保存到List集合中
            data.add(ppModel);
        }
        //返回数据
        return data;
    }

	//读取图片到硬盘中保存(如果是读取文件也是根据具体url读取即可)
	private static void readAndDownloadImgByte(String filePath,String imgUrl) {
		// 若指定文件夹没有，则先创建
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 截取图片文件名
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());
        //截取图片文件后缀名
        String fileNameSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));  
        //截取 https://image2.thepaper.cn/image/9/307/721.jpg 中image后面部分
        String fileNameN = imgUrl.substring(imgUrl.indexOf("cn") + 9, imgUrl.lastIndexOf("."));
        String[] nFileName=fileNameN.split("/");
        StringBuilder appendFileName=new StringBuilder();
        for(String n:nFileName){
        	appendFileName.append(n);
        }
        appendFileName.append(fileNameSuffix);
        System.out.println(appendFileName.toString());

        try {
            // 文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
            String urlTail = URLEncoder.encode(fileName.toString(), "UTF-8");
            // 因此要将加号转化为UTF-8格式的%20
            imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1) + urlTail.replaceAll("\\+", "\\%20");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 写出的路径
        File file = new File(filePath + File.separator + appendFileName.toString());

        try {
            // 获取图片URL
            URL url = new URL(imgUrl);
            // 获得连接
            URLConnection connection = url.openConnection();
            // 设置10秒的相应时间
            connection.setConnectTimeout(10 * 1000);
            // 获得输入流
            InputStream in = connection.getInputStream();
            // 获得输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            // 构建缓冲区
            byte[] buf = new byte[1024];
            int size;
            // 写入到文件
            while (-1 != (size = in.read(buf))) {
                out.write(buf, 0, size);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

}
