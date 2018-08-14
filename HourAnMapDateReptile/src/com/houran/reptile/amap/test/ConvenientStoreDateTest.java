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
	
	
	//�Ӹߵµ�ͼ��ȡ����  ���С�������
	@Test
	public void getConvenientStoreDate(){
		List<Shop> listShop =new ArrayList<>();
        //���������ڶ���107��45�䡫108��51�䣬��γ22��13�䡫23��32��֮�䣬�������궫��108��22�䣬��γ22��48�䡣
        for(double i=107.45;i<=108.51;i=i+0.1){
            for(double j=22.13;j<=23.32;j=j+0.1){
                List<Shop> listShopSon =new ArrayList<>();
                double lonHead=i;
                double latHead=j;
                double lonTail=i+0.1;
                double latTail=j+0.1;
                String LonLat=lonHead+","+latHead+";"+lonTail+","+latTail;
                listShopSon =AddressLngLatExchange.initialDataShop(LonLat,"������",listShopSon);
                for(int n=0;n<listShopSon.size();n++){
                    System.out.println("���̵�ַ��"+listShopSon.get(n).getSpecificAddress());
                }
                if(listShopSon.size()>0){
                    listShop.addAll(listShopSon);
                }
                System.out.println("ListShop�Ĵ�С��"+listShop.size());
                double d =AddressLngLatExchange.Distance(lonHead,latHead,lonTail,latTail);
                System.out.println("�������"+d);

            }

        }
        
        for(Shop shop:listShop){
        	LOGGER.info("�������֣�"+shop.getShopName()+"\t���̵�ַ��"+shop.getSpecificAddress());
        }
        System.out.println("ListShop�Ĵ�С��"+listShop.size());
        AddressLngLatExchange.creatExcel(listShop);//дExcel��
	}
	
	
	//Excel�ж�ȡ���б���������
	@Test
	public void getConvenientStoreDateFromExcel(){
		List<Shop> shops=AddressLngLatExchange.readFile("D:\\geode\\�ߵ±������ͼ�������б���������.xls");
		 for(Shop shop:shops){
			 System.out.println("�������֣�"+shop.getShopName()+"\t���̵�ַ��"+shop.getSpecificAddress());
		 }
		
		System.out.println("ListShop�Ĵ�С��"+shops.size());
	}

}
