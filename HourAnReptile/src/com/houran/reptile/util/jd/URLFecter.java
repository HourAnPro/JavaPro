package com.houran.reptile.util.jd;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;

import com.houran.reptile.model.jd.JdModel;
import com.houran.reptile.parse.jd.JdParse;
import com.houran.reptile.util.HTTPUtils;


/***
 * 
 * @author 
 *
 */
public class URLFecter {
    public static List<JdModel> URLParser (HttpClient client, String url) throws Exception {
        //�������ս���������
        List<JdModel> JingdongData = new ArrayList<JdModel>();
        //��ȡ��վ��Ӧ��html�����������HTTPUtils��
        HttpResponse response = HTTPUtils.getRawHtml(client, url);      
        //��ȡ��Ӧ״̬��
        int StatusCode = response.getStatusLine().getStatusCode();
        //���״̬��Ӧ��Ϊ200�����ȡhtmlʵ�����ݻ���json�ļ�
        if(StatusCode == 200){
            String entity = EntityUtils.toString (response.getEntity(),"utf-8");    
            JingdongData = JdParse.getData(entity);
            EntityUtils.consume(response.getEntity());
        }else {
            //�������ĵ�ʵ��
            EntityUtils.consume(response.getEntity());
        }
        return JingdongData;
    }
}
