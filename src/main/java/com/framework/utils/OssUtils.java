package com.framework.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.biz.conf.Global;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：OssUtils.java 描述说明 ：阿里云OSS 操作类
 * 
 * 创建信息 : create by 刘佳佳 on 2015-11-16 上午10:47:02  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
public class OssUtils {
		
	public static String UploadFile(MultipartFile file,String key){
		// 初始化一个OSSClient
        OSSClient client = new OSSClient(Global.getConfig("ENDPOINT"),Global.getConfig("ACCESSKEY"), Global.getConfig("ACCESSKEYSECRET"));
	    InputStream content=null;
		try {
			content = file.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    // 创建上传Object的Metadata
	    ObjectMetadata meta = new ObjectMetadata();
	    // 必须设置ContentLength
	    meta.setContentLength(file.getSize());
	    // 上传Object.
	    PutObjectResult result = client.putObject("9900oss", key, content, meta);
	    // 打印ETag
	    return result.getETag();
	}
	
	/**
	 * 二维码上传
	 * @param key
	 * @param path
	 * @return
	 */
	public static String UploadFile2(String key,String path){
		// 初始化一个OSSClient
        OSSClient client = new OSSClient(Global.getConfig("ENDPOINT"),Global.getConfig("ACCESSKEY"), Global.getConfig("ACCESSKEYSECRET"));
	    File file2=new File(path);
	    PutObjectResult result=client.putObject("9900oss",key, file2);
	    // 打印ETag
	    return result.getETag();
	}
	
	public static String UploadSmallFile(File file,String key){
		// 初始化一个OSSClient
        OSSClient client = new OSSClient(Global.getConfig("ENDPOINT"),Global.getConfig("ACCESSKEY"), Global.getConfig("ACCESSKEYSECRET"));
        FileInputStream content=null;
        // 创建上传Object的Metadata
	    ObjectMetadata meta = new ObjectMetadata();
		try {
			content = new FileInputStream(file);
	    // 必须设置ContentLength
	    meta.setContentLength(file.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	    // 上传Object.
	    PutObjectResult result = client.putObject("9900oss", key, content, meta);
	    // 打印ETag
	    return result.getETag();
	}

    public static String UploadFileForUe(MultipartFile file,String key){
        // 初始化一个OSSClient
        OSSClient client = new OSSClient(Global.getConfig("ENDPOINT"),Global.getConfig("ACCESSKEY"), Global.getConfig("ACCESSKEYSECRET"));
        InputStream content=null;
        try {
            content = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        meta.setContentLength(file.getSize());
        // 上传Object.
        PutObjectResult result = client.putObject("9900oss", key, content, meta);
        // 打印ETag
        return result.getETag();
    }
}
