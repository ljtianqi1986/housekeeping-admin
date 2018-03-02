package com.framework.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * MD5加密工具类
 * 
 * @author 刘佳佳
 * 
 */
public class URLConectionUtil {

    private static Logger logger = LoggerFactory.getLogger(URLConectionUtil.class);

	public static final String GET_URL = "";
    public static final String POST_URL = "";

	/**
	 * 微信推送发货提醒接口调用
	 * @param open_id 				购买者user表openid
	 * @param code 					orderId或者detailId
	 * @param goodsName 		商品名称
	 * @param sendType 			物流运送方式
	 * @param remark 				状态提醒字符串
	 */
	public static void httpURLConectionGET(String open_id,String code,String goodsName,String sendType,String remark) {
        HttpURLConnection connection = null;
        BufferedReader br = null;
        try {
            URL url = new URL("http://51zhonghua.com/wx/wx_toSendOrderNotice.do?open_id="+open_id+"&code="+code+"&goodsName="+goodsName+"&sendType="+sendType+"&remark="+remark);    // 把字符串转换为URL请求地址
            
            connection = (HttpURLConnection) url.openConnection();// 打开连接
            connection.connect();// 连接会话
            // 获取输入流
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {// 循环读取流
                sb.append(line);
            }
            logger.debug(sb.toString());
        } catch (Exception e) {
           logger.error("",e);
        } finally {
            if(br != null){
                IOUtils.closeQuietly(br);//关闭流
            }
            if(connection != null ){
                connection.disconnect();// 断开连接
            }

        }
    }
    
	
	
	public static void httpURLConectionGETCmd(String open_id,String code,String goodsName,String sendType,String remark) {
		String url="http://51zhonghua.com/wx/wx_toSendOrderNotice.do?open_id="+open_id+"&code="+code+"&goodsName="+goodsName+"&sendType="+sendType+"&remark="+remark;
		String cmd = "cmd.exe /c start " + url;
		  
		try {   
			Process proc = Runtime.getRuntime().exec(cmd);
			proc.waitFor();
		}   
		catch (Exception e)   
		{
            logger.error("",e);
		}  
    }
	
	
	
	
    /**
     * * 接口调用  POST
     */
    public static void httpURLConnectionPOST (String open_id,String code,String goodsName,String sendType,String remark) {
        HttpURLConnection connection = null;
        DataOutputStream dataout = null;
        BufferedReader bf = null;
        try {
            URL url = new URL("http://51zhonghua.com/wx/wx_toSendOrderNotice.do?");
            // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
            connection = (HttpURLConnection) url.openConnection();// 此时cnnection只是为一个连接对象,待连接中
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            connection.setDoOutput(true);
            // 设置连接输入流为true
            connection.setDoInput(true);
            // 设置请求方式为post
            connection.setRequestMethod("POST");
            // post请求缓存设为false
            connection.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);
            // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
            // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            connection.connect();
            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
            dataout = new DataOutputStream(connection.getOutputStream());
            String parm = "open_id=" + URLEncoder.encode(open_id, "utf-8"); //URLEncoder.encode()方法  为字符串进行编码
            parm += "&code=" + URLEncoder.encode(code, "utf-8");
            parm += "&goodsName=" + URLEncoder.encode(goodsName, "utf-8");
            parm += "&sendType=" + URLEncoder.encode(sendType, "utf-8");
            parm += "&remark=" + URLEncoder.encode(remark, "utf-8");
            // 将参数输出到连接
            dataout.writeBytes(parm);
            // 输出完成后刷新并关闭流
            dataout.flush();
            logger.debug(Integer.valueOf(connection.getResponseCode()).toString());
            // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
            bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder(); // 用来存储响应数据
            // 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
                sb.append(bf.readLine());
            }
            logger.debug(sb.toString());
        } catch (Exception e) {
            logger.error("",e);
        } finally {
            if(dataout!=null){
                IOUtils.closeQuietly(dataout);
            }
            if(bf!=null){
                IOUtils.closeQuietly(bf);
            }
            if(connection!=null){
                connection.disconnect();
            }
        }
    }
    
    /**
     * 自动确认收货调用模板
     */
    public static void ReceivePOST (String open_id,String orderId,String acceptTime,String sendCompany,String sendCode) {
        HttpURLConnection connection = null;
        DataOutputStream dataout = null;
        BufferedReader bf = null;
        try {
            URL url = new URL("http://51zhonghua.com/wx/wx_toSendOrderAccept.do?");
            // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
            connection = (HttpURLConnection) url.openConnection();// 此时cnnection只是为一个连接对象,待连接中
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            connection.setDoOutput(true);
            // 设置连接输入流为true
            connection.setDoInput(true);
            // 设置请求方式为post
            connection.setRequestMethod("POST");
            // post请求缓存设为false
            connection.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);
            // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
            // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            connection.connect();
            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
            dataout = new DataOutputStream(connection.getOutputStream());
            String parm = "open_id=" + URLEncoder.encode(open_id, "utf-8"); //URLEncoder.encode()方法  为字符串进行编码
            parm += "&orderId=" + URLEncoder.encode(orderId, "utf-8");
            parm += "&acceptTime=" + URLEncoder.encode(acceptTime, "utf-8");
            parm += "&sendCompany=" + URLEncoder.encode(sendCompany, "utf-8");
            parm += "&sendCode =" + URLEncoder.encode(sendCode , "utf-8");
            // 将参数输出到连接
            dataout.writeBytes(parm);
            // 输出完成后刷新并关闭流
            dataout.flush();
            logger.debug(Integer.valueOf(connection.getResponseCode()).toString());
            // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
            bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder(); // 用来存储响应数据
            // 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
                sb.append(bf.readLine());
            }
            logger.debug(sb.toString());
        } catch (Exception e) {
            logger.error("",e);
        } finally {
            if(dataout!=null){
                IOUtils.closeQuietly(dataout);
            }
            if(bf!=null){
                IOUtils.closeQuietly(bf);
            }
            if(connection!=null){
                connection.disconnect();
            }
        }
    }
    
    /**
     * 新发货微信推送消息接口
     */
    public static void newHttpURLConnectionPOST (String open_id,String orderId,String sendTime,String addr) {
        HttpURLConnection connection = null;
        DataOutputStream dataout = null;
        BufferedReader bf = null;
        try {
            URL url = new URL("http://51zhonghua.com/wx/wx_toSendOrderSend.do?");
            // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
            connection = (HttpURLConnection) url.openConnection();// 此时cnnection只是为一个连接对象,待连接中
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            connection.setDoOutput(true);
            // 设置连接输入流为true
            connection.setDoInput(true);
            // 设置请求方式为post
            connection.setRequestMethod("POST");
            // post请求缓存设为false
            connection.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);
            // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
            // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            connection.connect();
            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
            dataout = new DataOutputStream(connection.getOutputStream());
            String parm = "open_id=" + URLEncoder.encode(open_id, "utf-8"); //URLEncoder.encode()方法  为字符串进行编码
            parm += "&orderId=" + URLEncoder.encode(orderId, "utf-8");
            parm += "&sendTime=" + URLEncoder.encode(sendTime, "utf-8");
            parm += "&addr=" + URLEncoder.encode(addr, "utf-8");
            // 将参数输出到连接
            dataout.writeBytes(parm);
            // 输出完成后刷新并关闭流
            dataout.flush();
            logger.debug(Integer.valueOf(connection.getResponseCode()).toString());
            // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
            bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder(); // 用来存储响应数据
            // 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
                sb.append(bf.readLine());
            }
            logger.debug(sb.toString());
        } catch (Exception e) {
            logger.error("",e);
        } finally {
            if(dataout!=null){
                IOUtils.closeQuietly(dataout);
            }
            if(bf!=null){
                IOUtils.closeQuietly(bf);
            }
            if(connection!=null){
                connection.disconnect();
            }
        }
    }
    
    
    /**
	 * base64编码
	 * 
	 * @param imageURL
	 * @return
	 */
	public static String Base64Encoder(String imageURL) {
		byte[] data = null;
		DataInputStream in=null;
		// 读取图片字节数组
		try {
			URL url = new URL(imageURL);/* 将网络资源地址传给,即赋值给url */
			/* 此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流 */
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			in = new DataInputStream(connection.getInputStream());
			data = new byte[in.available()];
			in.read(data);
			
		} catch (IOException e) {
            logger.error("",e);
		}finally{
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
    
	
	public static String encodeImgageToBase64(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	    ByteArrayOutputStream outputStream = null;
	    try {
	      BufferedImage bufferedImage = ImageIO.read(imageUrl);
	      outputStream = new ByteArrayOutputStream();
	      ImageIO.write(bufferedImage, "jpg", outputStream);
	    } catch (MalformedURLException e1) {
            logger.error("",e1);
	    } catch (IOException e) {
            logger.error("",e);
	    }
	    // 对字节数组Base64编码
	    BASE64Encoder encoder = new BASE64Encoder();
	    return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
	  }

    
    
    public static void main(String[] args) {
//     	httpURLConectionGET();
//         httpURLConnectionPOST();
    }
    
	 //参数自定义接口方法
    public static String httpURLConnectionPostDiy(String actionURL,Map<String, String> Param) {
        String msg = "失败";
    	try {
            URL url = new URL(actionURL+"?");
            // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 此时cnnection只是为一个连接对象,待连接中
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            connection.setDoOutput(true);
            // 设置连接输入流为true
            connection.setDoInput(true);
            // 设置请求方式为post
            connection.setRequestMethod("POST");
            // post请求缓存设为false
            connection.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);
            // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
            // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            connection.connect();
            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
            String parm ="";
            for (String key : Param.keySet()) {
            	parm +="&"+key+"=" + URLEncoder.encode(Param.get(key), "utf-8");
            }
            parm=parm.substring(1, parm.length());
            // 将参数输出到连接
            dataout.writeBytes(parm);
            // 输出完成后刷新并关闭流
            dataout.flush();
            dataout.close(); // 重要且易忽略步骤 (关闭流,切记!) 
            //流中获取字节并编码
            byte[] bytes = new byte[1024 * 1024];
            InputStream is = connection.getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0){
                    nTotalRead = nTotalRead + nRead;
                }
            }
            String str = new String(bytes, 0, nTotalRead, "utf-8");  
            msg = str;
            connection.disconnect(); // 销毁连接
        } catch (Exception e) {
            logger.error("",e);
        }
    	return msg;
    }

}
