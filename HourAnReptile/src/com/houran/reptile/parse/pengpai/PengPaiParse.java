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
 * ����jsoupץȡ��ҳͼƬ:https://www.cnblogs.com/zyoung/p/6579963.html
 * 
 * Jsoupͨ��URL��ȡ�ĵ�����ȡhref��������Ȼ���ܹ�IO����ȡ�ļ����ɣ�https://blog.csdn.net/qq_37521135/article/details/78783021
 * @author HourAn
 *
 */
public class PengPaiParse {
	
	public static List<PengPaiModel> getData (String html) throws Exception{
        //��ȡ�����ݣ�����ڼ�����
        List<PengPaiModel> data = new ArrayList<PengPaiModel>();
        //����Jsoup����
        Document doc = Jsoup.parse(html);
        //��ȡhtml��ǩ�е�����
        Elements elements=doc.select("div[id=listContent]").select("div[class=news_li]");
        //elements.remove(elements.size()-1);
        for (Element ele:elements) {
            String ppID=ele.attr("id");
            //�õ�ͼƬurl
            Elements  imgElements =ele.select("div[class=news_tu]").select("a[class=tiptitleImg]").select("img");
            String fielPath="D:/imges";
          //����һ������ʹ��Modelֱ�ӽ��з�װ
            PengPaiModel ppModel=new PengPaiModel();            
            for(Element imgEle:imgElements){
            	String imgUrl=imgEle.attr("src");
            	imgUrl="https:"+imgUrl;
            	readAndDownloadImgByte(fielPath,imgUrl);
            	ppModel.setImgUrl(imgUrl);
            }
           
            
            String title=ele.select("h2").select("a").text();
            String content=ele.select("p").text();
            
            //�����ֵ
            ppModel.setId(ppID);
            ppModel.setTitle(title);
            ppModel.setContent(content);
     
            //��ÿһ�������ֵ�����浽List������
            data.add(ppModel);
        }
        //��������
        return data;
    }

	//��ȡͼƬ��Ӳ���б���(����Ƕ�ȡ�ļ�Ҳ�Ǹ��ݾ���url��ȡ����)
	private static void readAndDownloadImgByte(String filePath,String imgUrl) {
		// ��ָ���ļ���û�У����ȴ���
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // ��ȡͼƬ�ļ���
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());
        //��ȡͼƬ�ļ���׺��
        String fileNameSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));  
        //��ȡ https://image2.thepaper.cn/image/9/307/721.jpg ��image���沿��
        String fileNameN = imgUrl.substring(imgUrl.indexOf("cn") + 9, imgUrl.lastIndexOf("."));
        String[] nFileName=fileNameN.split("/");
        StringBuilder appendFileName=new StringBuilder();
        for(String n:nFileName){
        	appendFileName.append(n);
        }
        appendFileName.append(fileNameSuffix);
        System.out.println(appendFileName.toString());

        try {
            // �ļ���������������Ļ��߿ո���������Ҫ���д������ո��ֻᱻURLEncoderת��Ϊ�Ӻ�
            String urlTail = URLEncoder.encode(fileName.toString(), "UTF-8");
            // ���Ҫ���Ӻ�ת��ΪUTF-8��ʽ��%20
            imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1) + urlTail.replaceAll("\\+", "\\%20");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // д����·��
        File file = new File(filePath + File.separator + appendFileName.toString());

        try {
            // ��ȡͼƬURL
            URL url = new URL(imgUrl);
            // �������
            URLConnection connection = url.openConnection();
            // ����10�����Ӧʱ��
            connection.setConnectTimeout(10 * 1000);
            // ���������
            InputStream in = connection.getInputStream();
            // ��������
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            // ����������
            byte[] buf = new byte[1024];
            int size;
            // д�뵽�ļ�
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
