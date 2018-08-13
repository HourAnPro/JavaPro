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
    //log4j��ʹ�ã�������뿴֮ǰд������
    static final Log logger = LogFactory.getLog(JdongMain.class);
    public static void main(String[] args) throws Exception {
        //��ʼ��һ��httpclient    	
        HttpClient client = new DefaultHttpClient();
        //����Ҫ��ȡ��һ����ַ��������Դ����ݿ��г�ȡ���ݣ�Ȼ������ѭ����������ȡһ��URL����
        String url="http://search.jd.com/Search?keyword=python%20%E4%B9%A6&enc=utf-8&wq=python%20%E4%B9%A6&pvid=ded7611a69094099bc52c944d8d9f1bf";
        //ץȡ������
        List<JdModel> bookdatas=URLFecter.URLParser(client, url);
        //ѭ�����ץȡ������
        for (JdModel jd:bookdatas) {
            logger.info("bookID:"+jd.getBookID()+"\t"+"bookPrice:"+jd.getBookPrice()+"\t"+"bookName:"+jd.getBookName());
        }
        //��ץȡ�����ݲ������ݿ�
        //MYSQLControl.executeInsert(bookdatas);
    }
}

