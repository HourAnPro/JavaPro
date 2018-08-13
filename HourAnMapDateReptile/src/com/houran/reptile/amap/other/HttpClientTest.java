package com.houran.reptile.amap.other;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.houran.reptile.amap.utils.HttpUtils;

public class HttpClientTest {
	
	@Test
	public void urlTest(){
		Map<String, String> params = new HashMap<String, String>();
		//p=1&q=java&t=blog&domain=&o=&s=&u=&l=&f=&rbg=0
		params.put("p", "2");
		params.put("q", "java");
		params.put("t", null);
		params.put("domain", null);
		params.put("o", null);
		params.put("s", null);
		params.put("u", null);
		params.put("l", null);
		params.put("f", null);
		//params.put("rbg", "0");
		String result = HttpUtils.URLGet("https://so.csdn.net/so/search/s.do",params,"UTF-8");
        //JSONObject jsonObject = JSONObject.parseObject(result);
        System.out.println("result:"+result);
        //System.out.println("jsonObject:"+jsonObject);
	}
	
	@Test
	public void urlTest2(){
		Map<String, String> params = new HashMap<String, String>();		
		params.put("searchword", "Æû³µ");
		//params.put("rbg", "0");
		String result = HttpUtils.URLPost("http://www.gsxt.gov.cn/corp-query-search-1.html",params,"UTF-8");
        //JSONObject jsonObject = JSONObject.parseObject(result);
        System.out.println("result:"+result);
        //System.out.println("jsonObject:"+jsonObject);
	}

}
