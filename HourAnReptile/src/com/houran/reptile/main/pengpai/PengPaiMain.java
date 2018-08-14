package com.houran.reptile.main.pengpai;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.houran.reptile.model.jd.JdModel;
import com.houran.reptile.model.pengpai.PengPaiModel;
import com.houran.reptile.parse.pengpai.PengPaiParse;
import com.houran.reptile.util.jd.URLFecter;
import com.houran.reptile.util.pengpai.PPURLFecter;

//获取澎湃新闻 条目
public class PengPaiMain {
	
	static final Log logger = LogFactory.getLog(PengPaiMain.class);
	
	public static void main(String[] args) throws Exception {
		//初始化一个httpclient    	
        HttpClient client = new DefaultHttpClient();
        //我们要爬取的一个地址，这里可以从数据库中抽取数据，然后利用循环，可以爬取一个URL队列
        String url="https://www.thepaper.cn/";
        //抓取的数据
        List<PengPaiModel> newdatas=PPURLFecter.URLParser(client, url);
        //循环输出抓取的数据
        int i=1;
        for (PengPaiModel pp:newdatas) {
            logger.info("序号："+i+"\t"+"ppID:"+pp.getId()+"\t"+"title:"+pp.getTitle()+"\t"+"content:"+pp.getContent()+"\t"+"imgUrl:"+pp.getImgUrl());
            i++;
        }
        //将抓取的数据插入数据库
        //MYSQLControl.executeInsert(bookdatas);				
	}

}
