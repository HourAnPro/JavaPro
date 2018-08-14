package com.houran.reptile.util.pengpai;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;

import com.houran.reptile.model.jd.JdModel;
import com.houran.reptile.model.pengpai.PengPaiModel;
import com.houran.reptile.parse.jd.JdParse;
import com.houran.reptile.parse.pengpai.PengPaiParse;
import com.houran.reptile.util.HTTPUtils;

public class PPURLFecter {
	
	public static List<PengPaiModel> URLParser (HttpClient client, String url) throws Exception {
        //�������ս���������
        List<PengPaiModel> ppData = new ArrayList<PengPaiModel>();
        //��ȡ��վ��Ӧ��html�����������HTTPUtils��
        HttpResponse response = HTTPUtils.getRawHtml(client, url);      
        //��ȡ��Ӧ״̬��
        int StatusCode = response.getStatusLine().getStatusCode();
        //���״̬��Ӧ��Ϊ200�����ȡhtmlʵ�����ݻ���json�ļ�
        if(StatusCode == 200){
            String entity = EntityUtils.toString (response.getEntity(),"utf-8");    
            ppData = PengPaiParse.getData(entity);
            EntityUtils.consume(response.getEntity());
        }else {
            //�������ĵ�ʵ��
            EntityUtils.consume(response.getEntity());
        }
        return ppData;
    }

}
