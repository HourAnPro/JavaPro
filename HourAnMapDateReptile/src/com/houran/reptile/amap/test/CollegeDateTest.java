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
	
		//从高德地图上取数据  高校 大学
		@Test
		public void getCollegeDate(){
			List<College> listCollege =new ArrayList<>();
	        //南宁，介于东经107°45′～108°51′，北纬22°13′～23°32′之间，地理坐标东经108°22′，北纬22°48′。
	        for(double i=107.45;i<=108.51;i=i+0.1){
	            for(double j=22.13;j<=23.32;j=j+0.1){
	                List<College> listCollegeSon =new ArrayList<>();
	                double lonHead=i;
	                double latHead=j;
	                double lonTail=i+0.1;
	                double latTail=j+0.1;
	                String LonLat=lonHead+","+latHead+";"+lonTail+","+latTail;
	                listCollegeSon =AddressLngLatExchange.initialDataCollege(LonLat,"高等院校",listCollegeSon);
	                for(int n=0;n<listCollegeSon.size();n++){
	                    System.out.println("学校地址："+listCollegeSon.get(n).getAddress());
	                }
	                if(listCollegeSon.size()>0){
	                	listCollege.addAll(listCollegeSon);
	                }
	                System.out.println("listCollege的大小："+listCollege.size());
	                double d =AddressLngLatExchange.Distance(lonHead,latHead,lonTail,latTail);
	                System.out.println("两点距离"+d);

	            }

	        }
	        
	        for(College college:listCollege){
	        	System.out.println("学校名字："+college.getCollegeName()+"\t学校地址："+college.getAddress());
	        }
	        System.out.println("listCollege的大小："+listCollege.size());
	        //AddressLngLatExchange.creatExcel(listShop);//写Excel中
		}

}
