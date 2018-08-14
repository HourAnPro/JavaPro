package com.houran.reptile.amap.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.houran.reptile.amap.date.AddressLngLatExchange;
import com.houran.reptile.amap.pojo.Shop;

public class ConvenientStoreDateTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConvenientStoreDateTest.class);
	
	
	//从高德地图上取数据  超市、便利店
	@Test
	public void getConvenientStoreDate(){
		List<Shop> listShop =new ArrayList<>();
        //南宁，介于东经107°45′～108°51′，北纬22°13′～23°32′之间，地理坐标东经108°22′，北纬22°48′。
        for(double i=107.45;i<=108.51;i=i+0.1){
            for(double j=22.13;j<=23.32;j=j+0.1){
                List<Shop> listShopSon =new ArrayList<>();
                double lonHead=i;
                double latHead=j;
                double lonTail=i+0.1;
                double latTail=j+0.1;
                String LonLat=lonHead+","+latHead+";"+lonTail+","+latTail;
                listShopSon =AddressLngLatExchange.initialDataShop(LonLat,"便利店",listShopSon);
                for(int n=0;n<listShopSon.size();n++){
                    System.out.println("店铺地址："+listShopSon.get(n).getSpecificAddress());
                }
                if(listShopSon.size()>0){
                    listShop.addAll(listShopSon);
                }
                System.out.println("ListShop的大小："+listShop.size());
                double d =AddressLngLatExchange.Distance(lonHead,latHead,lonTail,latTail);
                System.out.println("两点距离"+d);

            }

        }
        
        for(Shop shop:listShop){
        	LOGGER.info("店铺名字："+shop.getShopName()+"\t店铺地址："+shop.getSpecificAddress());
        }
        System.out.println("ListShop的大小："+listShop.size());
        AddressLngLatExchange.creatExcel(listShop);//写Excel中
	}
	
	
	//Excel中读取超市便利店数据
	@Test
	public void getConvenientStoreDateFromExcel(){
		List<Shop> shops=AddressLngLatExchange.readFile("D:\\geode\\高德便利店地图——超市便利店数据.xls");
		 for(Shop shop:shops){
			 System.out.println("店铺名字："+shop.getShopName()+"\t店铺地址："+shop.getSpecificAddress());
		 }
		
		System.out.println("ListShop的大小："+shops.size());
	}

}
