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

//��ȡ�������� ��Ŀ
public class PengPaiMain {
	
	static final Log logger = LogFactory.getLog(PengPaiMain.class);
	
	public static void main(String[] args) throws Exception {
		//��ʼ��һ��httpclient    	
        HttpClient client = new DefaultHttpClient();
        //����Ҫ��ȡ��һ����ַ��������Դ����ݿ��г�ȡ���ݣ�Ȼ������ѭ����������ȡһ��URL����
        String url="https://www.thepaper.cn/";
        //ץȡ������
        List<PengPaiModel> newdatas=PPURLFecter.URLParser(client, url);
        //ѭ�����ץȡ������
        int i=1;
        for (PengPaiModel pp:newdatas) {
            logger.info("��ţ�"+i+"\t"+"ppID:"+pp.getId()+"\t"+"title:"+pp.getTitle()+"\t"+"content:"+pp.getContent()+"\t"+"imgUrl:"+pp.getImgUrl());
            i++;
        }
        //��ץȡ�����ݲ������ݿ�
        //MYSQLControl.executeInsert(bookdatas);				
	}

}
