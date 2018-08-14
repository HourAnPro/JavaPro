package com.houran.reptile.amap.date;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.houran.reptile.amap.pojo.College;
import com.houran.reptile.amap.pojo.Shop;
import com.houran.reptile.amap.utils.HttpUtils;

import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class AddressLngLatExchange {
	
	private static final String KEY = "389880a06e3f893ea46036f030c94700";
    private static final String OUTPUT = "JSON";
    private static final String GET_LNG_LAT_URL = "http://restapi.amap.com/v3/geocode/geo";
 
    private static final String GET_LNG_PIO_URL = "http://restapi.amap.com/v3/place/polygon";
 
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressLngLatExchange.class);
 
 
    //��ȡָ���ص㾭γ��
    public static String[] getLngLatFromOneAddr(String address){
        if(StringUtils.isBlank(address)) {
            LOGGER.error("��ַ��" + address + "��Ϊnull���߿�");
            return null;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("address", address);
        params.put("output", OUTPUT);
        params.put("key", KEY);
        String result = HttpUtils.URLPost(GET_LNG_LAT_URL,params,"");
        JSONObject jsonObject = JSONObject.parseObject(result);
        String[] lngLatArr = new String[2];
        //�õ����ر��ĵ�statusֵ���ߵµĸýӿڷ���ֵ��������0-����ʧ�ܣ�1-����ɹ���
        int status = Integer.valueOf(jsonObject.getString("status"));
        if(status == 1) {
            JSONArray jsonArray = jsonObject.getJSONArray("geocodes");
            for(int i = 0; i < jsonArray.size(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String lngLat = json.getString("location");
                 lngLatArr = lngLat.split(",");
            }
        } else {
            String errorMsg = jsonObject.getString("info");
            LOGGER.error("��ַ��" + address + "��" + errorMsg);
        }
        return lngLatArr;
    }
    
    //��ȡ����  ��У ��ѧ
    public static List<College> initialDataCollege(String lonLat, String keyword, List<College> collegeListSon){
        if(StringUtils.isBlank(keyword)) {
            LOGGER.error("��ַ��" + keyword + "��Ϊnull���߿�");
        }
        Map<String, String> params = new HashMap<String, String>();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        params.put("polygon", lonLat);//"118.21,29.11;120.30,30.33"
        params.put("output", OUTPUT);
        //params.put("keywords", keyword);//����� types �� keywords ��������������һ�������򷵻�poi��ȫ
        params.put("types", keyword);
        params.put("offset", "20");//����ڻ����ƫ��λ��
        params.put("page", "1");
        params.put("key", KEY);
        String result = HttpUtils.URLGet(GET_LNG_PIO_URL,params,"UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(result);
        int statusOne = Integer.valueOf(jsonObject.getString("status"));
        //��һ�λ�ȡ����ʱ�����ж�
        if(statusOne==1){
            int count=Integer.valueOf(jsonObject.getString("count"));
            int pageNumber=count/20;
            int remainder=count%20;
            if(remainder>0)pageNumber=pageNumber+1;
            for(int i=1;i<=pageNumber;i++){
                params.put("page", String.valueOf(i));
                result = HttpUtils.URLGet(GET_LNG_PIO_URL,params,"UTF-8");
                JSONObject jsonObject2 = JSONObject.parseObject(result);
                System.out.println("+++++++++"+result);
                //�õ����ر��ĵ�statusֵ���ߵµĸýӿڷ���ֵ��������0-����ʧ�ܣ�1-����ɹ���
                int status = Integer.valueOf(jsonObject2.getString("status"));
                if(status == 1) {
                    JSONArray jsonArray = jsonObject2.getJSONArray("pois");
                    if(jsonArray.size()>0){
                        for(int j =0;j<jsonArray.size();j++){
                            College college =new College();
                            JSONObject jsonObject1 =jsonArray.getJSONObject(j);
                            college.setCollegeName(jsonObject1.getString("name"));
                            college.setAddress(jsonObject1.getString("address"));
                            college.setId(jsonObject1.getString("id"));
                            String [] initLonLat =jsonObject1.getString("location").split(",");
                            college.setLongitude(initLonLat[0]);
                            college.setLatitude(initLonLat[1]);
                            collegeListSon.add(college);
                            //DBObject  doci = new BasicDBObject("shopId", "300"+i).append("shopName", "��������"+i).append("shopStatus",0).append("specificAddress","����"+i).append("gps", new Point(new Position(lon, lat)));
 
                        }
                    }
 
                } else {
                        String errorMsg = jsonObject.getString("info");
                        LOGGER.error("��ַ��" + keyword + "��" + errorMsg);
                }
            }
 
        }
        return collegeListSon;
 
    }
 
    //��ȡ����  ���С�������
    public static List<Shop> initialDataShop(String lonLat, String keyword, List<Shop> shopListSon){
        if(StringUtils.isBlank(keyword)) {
            LOGGER.error("��ַ��" + keyword + "��Ϊnull���߿�");
        }
        Map<String, String> params = new HashMap<String, String>();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        params.put("polygon", lonLat);//"118.21,29.11;120.30,30.33"
        params.put("output", OUTPUT);
        params.put("keywords", keyword);//����� types �� keywords ��������������һ�������򷵻�poi��ȫ       
        //params.put("types", keyword);
        params.put("offset", "20");//����ڻ����ƫ��λ��
        params.put("page", "1");
        params.put("key", KEY);
        String result = HttpUtils.URLGet(GET_LNG_PIO_URL,params,"UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(result);
        int statusOne = Integer.valueOf(jsonObject.getString("status"));
        //��һ�λ�ȡ����ʱ�����ж�
        if(statusOne==1){
            int count=Integer.valueOf(jsonObject.getString("count"));
            int pageNumber=count/20;
            int remainder=count%20;
            if(remainder>0)pageNumber=pageNumber+1;
            for(int i=1;i<=pageNumber;i++){
                params.put("page", String.valueOf(i));
                result = HttpUtils.URLGet(GET_LNG_PIO_URL,params,"UTF-8");
                JSONObject jsonObject2 = JSONObject.parseObject(result);
                System.out.println("+++++++++"+result);
                //�õ����ر��ĵ�statusֵ���ߵµĸýӿڷ���ֵ��������0-����ʧ�ܣ�1-����ɹ���
                int status = Integer.valueOf(jsonObject2.getString("status"));
                if(status == 1) {
                    JSONArray jsonArray = jsonObject2.getJSONArray("pois");
                    if(jsonArray.size()>0){
                        for(int j =0;j<jsonArray.size();j++){
                            Shop shop =new Shop();
                            JSONObject jsonObject1 =jsonArray.getJSONObject(j);
                            shop.setShopName(jsonObject1.getString("name"));
                            shop.setSpecificAddress(jsonObject1.getString("address"));
                            shop.setId(jsonObject1.getString("id"));
                            String [] initLonLat =jsonObject1.getString("location").split(",");
                            shop.setLongitude(initLonLat[0]);
                            shop.setLatitude(initLonLat[1]);
                            shopListSon.add(shop);
                            //DBObject  doci = new BasicDBObject("shopId", "300"+i).append("shopName", "��������"+i).append("shopStatus",0).append("specificAddress","����"+i).append("gps", new Point(new Position(lon, lat)));
 
                        }
                    }
 
                } else {
                        String errorMsg = jsonObject.getString("info");
                        LOGGER.error("��ַ��" + keyword + "��" + errorMsg);
                }
            }
 
        }
        return shopListSon;
 
    }
 
    //�Ӹߵµ�ͼ��ȡ����  ���С�������
    /*public static void main(String[] args) {
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
                listShopSon =initialDataShop(LonLat,"������",listShopSon);
                for(int n=0;n<listShopSon.size();n++){
                    System.out.println("���̵�ַ��"+listShopSon.get(n).getSpecificAddress());
                }
                if(listShopSon.size()>0){
                    listShop.addAll(listShopSon);
                }
                System.out.println("ListShop�Ĵ�С��"+listShop.size());
                double d =Distance(lonHead,latHead,lonTail,latTail);
                System.out.println("�������"+d);

            }

        }
        
        for(Shop shop:listShop){
        	LOGGER.info("�������֣�"+shop.getShopName()+"\t���̵�ַ��"+shop.getSpecificAddress());
        }
        System.out.println("ListShop�Ĵ�С��"+listShop.size());
        creatExcel(listShop);
    }*/
    
 
    /*public static void main(String[] args) {
        readFile("D:\\geode\\�ߵ±������ͼ�������б���������.xls");
    }*/
 
 
    //д��excel��
    public static void creatExcel(List<Shop> shopList){
        HSSFWorkbook workbook = new HSSFWorkbook();
        //�ڶ�������workbook�д���һ��sheet��Ӧexcel�е�sheet
        HSSFSheet sheet = workbook.createSheet("�ߵµ�ͼ���ݡ������б�����");      
        //����������sheet������ӱ�ͷ��0�У��ϰ汾��poi��sheet������������
        HSSFRow row = sheet.createRow(0);
        //���Ĳ���������Ԫ�����ñ�ͷ
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("����id");
        cell = row.createCell(1);
        cell.setCellValue("��������");
        cell = row.createCell(2);
        cell.setCellValue("���̵�ַ");
        cell = row.createCell(3);
        cell.setCellValue("����");
        cell = row.createCell(4);
        cell.setCellValue("γ��");
 
        //���岽��д��ʵ�����ݣ�ʵ��Ӧ������Щ���ݴ����ݿ�õ�,�����װ���ݣ����ϰ����󡣶��������ֵ��Ӧ���ÿ�е�ֵ
        for (int i = 0; i < shopList.size(); i++) {
            HSSFRow row1 = sheet.createRow(i + 1);
            Shop shop = shopList.get(i);
            //������Ԫ����ֵ
            row1.createCell(0).setCellValue(shop.getId());
            row1.createCell(1).setCellValue(shop.getShopName());
            row1.createCell(2).setCellValue(shop.getSpecificAddress());
            row1.createCell(3).setCellValue(shop.getLongitude());
            row1.createCell(4).setCellValue(shop.getLatitude());
        }
 
        //���ļ����浽ָ����λ��
        try {
            FileOutputStream fos = new FileOutputStream("D:\\geode\\�ߵ±������ͼ�������б���������.xls");//���Զ����� �ߵ±������ͼ�������б���������.xls
            workbook.write(fos);
            System.out.println("д��ɹ�");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    //����֮��ľ���
    public static double Distance(double long1, double lat1, double long2, double lat2) {
        double a, b, R;
        R =6371; // ����뾶 6371km
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * R
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));
        BigDecimal bigDecimal = new BigDecimal(d*1000);
        Double din = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return din ;
    }
 
    public static List<Shop> readFile(String filename){
        List<Shop> shopList =new ArrayList<>();
        Workbook wb=null;
        Cell cell=null;
        try {
            File f=new File(filename);
            InputStream in=new FileInputStream(f);             //����������
            wb= Workbook.getWorkbook(in);               //��ȡExcel�ļ�����
            jxl.Sheet s=wb.getSheet(0);                        //��ȡ�ļ���ָ��������Ĭ��Ϊ��һ��
            String value=null;
            for(int i=1;i<s.getRows();i++){//��ͷĿ¼����Ҫ���ӵ�һ�п�ʼ
                Shop shop =new Shop();
                for(int j=0;j<s.getColumns();j++){
                    cell=s.getCell(j, i);
                    value=cell.getContents();
                    if(j==0){
                        shop.setId(value);
                    }else if(j==1){
                        shop.setShopName(value);
                    }else if(j==2){
                        shop.setSpecificAddress(value);
                    }else if(j==3){
                    shop.setLongitude(value);
                    }else if(j==4){
                    shop.setLatitude(value);
                    }
                  //  System.out.println("value��"+value);
                }
                shopList.add(shop);
            }
 
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BiffException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return shopList;
    }
	
}
