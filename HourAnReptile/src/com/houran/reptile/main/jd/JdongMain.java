package com.houran.reptile.main.jd;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.houran.reptile.db.MYSQLControl;
import com.houran.reptile.model.jd.JdModel;
import com.houran.reptile.util.jd.URLFecter;



/***
 * 
 * @author 
 *
 */
public class JdongMain {
    //log4j的使用，不会的请看之前写的文章
    static final Log logger = LogFactory.getLog(JdongMain.class);
    public static void main(String[] args) throws Exception {
        //初始化一个httpclient    	
        HttpClient client = new DefaultHttpClient();
        //我们要爬取的一个地址，这里可以从数据库中抽取数据，然后利用循环，可以爬取一个URL队列
        String url="http://search.jd.com/Search?keyword=python%20%E4%B9%A6&enc=utf-8&wq=python%20%E4%B9%A6&pvid=ded7611a69094099bc52c944d8d9f1bf";
        //抓取的数据
        List<JdModel> bookdatas=URLFecter.URLParser(client, url);
        //循环输出抓取的数据
        for (JdModel jd:bookdatas) {
            logger.info("bookID:"+jd.getBookID()+"\t"+"bookPrice:"+jd.getBookPrice()+"\t"+"bookName:"+jd.getBookName());
        }
        //将抓取的数据插入数据库
        //MYSQLControl.executeInsert(bookdatas);
    }
}

