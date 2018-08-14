package com.houran.reptile.amap.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.houran.reptile.amap.date.AddressLngLatExchange;
import com.houran.reptile.amap.pojo.College;
import com.houran.reptile.amap.pojo.Shop;

public class CollegeDateTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CollegeDateTest.class);
	
		//�Ӹߵµ�ͼ��ȡ����  ��У ��ѧ
		@Test
		public void getCollegeDate(){
			List<College> listCollege =new ArrayList<>();
	        //���������ڶ���107��45�䡫108��51�䣬��γ22��13�䡫23��32��֮�䣬�������궫��108��22�䣬��γ22��48�䡣
	        for(double i=107.45;i<=108.51;i=i+0.1){
	            for(double j=22.13;j<=23.32;j=j+0.1){
	                List<College> listCollegeSon =new ArrayList<>();
	                double lonHead=i;
	                double latHead=j;
	                double lonTail=i+0.1;
	                double latTail=j+0.1;
	                String LonLat=lonHead+","+latHead+";"+lonTail+","+latTail;
	                listCollegeSon =AddressLngLatExchange.initialDataCollege(LonLat,"�ߵ�ԺУ",listCollegeSon);
	                for(int n=0;n<listCollegeSon.size();n++){
	                    System.out.println("ѧУ��ַ��"+listCollegeSon.get(n).getAddress());
	                }
	                if(listCollegeSon.size()>0){
	                	listCollege.addAll(listCollegeSon);
	                }
	                System.out.println("listCollege�Ĵ�С��"+listCollege.size());
	                double d =AddressLngLatExchange.Distance(lonHead,latHead,lonTail,latTail);
	                System.out.println("�������"+d);

	            }

	        }
	        
	        for(College college:listCollege){
	        	System.out.println("ѧУ���֣�"+college.getCollegeName()+"\tѧУ��ַ��"+college.getAddress());
	        }
	        System.out.println("listCollege�Ĵ�С��"+listCollege.size());
	        //AddressLngLatExchange.creatExcel(listShop);//дExcel��
		}

}
