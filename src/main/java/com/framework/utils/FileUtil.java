package com.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

/**
 * 文件操作类
 * @author lj_ma
 *
 */
public class FileUtil {
	
	 /**
     * 创建文件目录
     * @param   sPath   文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean createFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (!file.exists()) {
            file.mkdir();
            flag = true;
        }
        return flag;
    }
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    /**
     * 获取随机产生一个指定长度的文件名
     * @return
     */
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefABCDEF1234567890";   
        Random random = new Random();   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i < length; i++) {   
            int number = random.nextInt(base.length());   
            sb.append(base.charAt(number));   
        }   
        return sb.toString();   
     } 
    
    
    /**
     * 获取随机产生一个指定长度的文件名(只包含数字)
     * @param length
     * @return
     */
    public static String getRandomString2(int length) { //length表示生成字符串的长度
        String base = "1234567890";   
        Random random = new Random();   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i < length; i++) {   
            int number = random.nextInt(base.length());   
            sb.append(base.charAt(number));   
        }   
        return sb.toString();   
     }  
    
    
    /**
     * 根据日期 以及5位随机数 生成 单号
     * @param prv
     * @return
     */
    public static String getListNum(String prv){
    	 Calendar   calendar =Calendar.getInstance();
		 String   year =String.valueOf(calendar.get(Calendar.YEAR));   
		 String   month =String.valueOf(calendar.get(Calendar.MONTH)+1);   
		 String   day =String.valueOf(calendar.get(Calendar.DATE));  
		 String   hour=String.valueOf(calendar.get(Calendar.HOUR));
		 String   minute=String.valueOf(calendar.get(Calendar.MINUTE));
		 String   second=String.valueOf(calendar.get(Calendar.SECOND));
		 String rr=getRandomString2(2);
		 StringBuilder sb=new StringBuilder();
    	return sb.append(prv).append(year).append(month).append(day).append(hour).append(minute).append(second).append(rr).toString();
    }
    
    /**
     * 根据日期 生成 文件
     * @param prv
     * @return
     */
    public static String getFileNum(){
    	 Calendar   calendar =Calendar.getInstance();
		 String   year =String.valueOf(calendar.get(Calendar.YEAR));   
		 String   month =String.valueOf(calendar.get(Calendar.MONTH)+1);   
		 String   day =String.valueOf(calendar.get(Calendar.DATE));  
		 StringBuilder sb=new StringBuilder();
    	return sb.append(year).append(month).append(day).toString();
    }
    
    
    /**
     * 根据日期 生成 文件夹
     * @param prv
     * @return
     */
    public static String getFileDir(){
    	 Calendar   calendar =Calendar.getInstance();
		 String   year =String.valueOf(calendar.get(Calendar.YEAR));   
		 String   month =String.valueOf(calendar.get(Calendar.MONTH)+1);   
		 String   day =String.valueOf(calendar.get(Calendar.DATE));  
		 StringBuilder sb=new StringBuilder();
    	return sb.append(year).append("/").append(month).append("/").append(day).append("/").toString();
    }
    
    /**
     * 读取properties 文件中的内容
     * @param key
     * @return
     */
    public static String getProperties(String key){
    	Properties prop = new Properties();
    	String value="";
    	try {   
    		//URL fileUrl=Thread.currentThread().getContextClassLoader().getResource("D:/glob.properties");//本项目路径
    		//fileUrl.getPath()
    		prop.load(new FileInputStream("conf/jdbc.properties"));
               value=prop.getProperty(key);
    	 } catch(IOException e) {   
    		     e.printStackTrace(); 
         }   
    	return value;
    }
}
