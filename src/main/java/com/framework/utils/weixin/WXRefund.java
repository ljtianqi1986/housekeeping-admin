package com.framework.utils.weixin;

import com.framework.utils.ConfigUtil;
import com.framework.utils.weixin.pay.MD5Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

/**
 * 微信调起支付类
 * @author GengLong
 *
 */
public class WXRefund
{
	private static Logger logger = LoggerFactory.getLogger(WXRefund.class);

	private static String refund_url = "https://pay.swiftpass.cn/pay/gateway";
	private static String Queryrefund_url = "https://pay.swiftpass.cn/pay/gateway";
    private String service="unified.trade.refund";
	private String mch_id;//微信支付分配的商户号
    private String out_trade_no;//商户系统内部的订单号,transaction_id、out_trade_no二选一，如果同时存在优先级：transaction_id> out_trade_no
    private String transaction_id;//兴业银行订单号
    private String out_refund_no;//商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
    private int total_fee;//订单总金额，单位为分，只能为整数
    private int refund_fee;//退款总金额，订单总金额，单位为分，只能为整数
    private String nonce_str;//随机字符串，不长于32位
    private String sign;//签名
	private String op_user_id;//操作员帐号, 默认为商户号
    private String refund_channel;//ORIGINAL-原路退款，默认  BALANCE-余额
	//private String partnerKey;
	
	//表示请求器是否已经做了初始化工作
    //private boolean hasInit = false;
  	////HTTP请求器
    private CloseableHttpClient closeableHttpClient;
    //@Autowired
    //private BaseDaoI<TSysLog> sysLogDao;

	//private static String certPassword=null;
	//private static String getCertPassword(){
	//	if(certPassword!=null){
	//		return certPassword;
	//	} else
	//	if(certPassword==null||"".equals(certPassword)){
	//		synchronized (WXRefund.class){
	//			if(certPassword==null||"".equals(certPassword)){
	//				ResourceBundle bundle = ResourceBundle.getBundle("conf/jdbc");
	//				certPassword = bundle.getString("cert_password");
	//				return certPassword;
	//			}
	//		}
	//	}
	//	return certPassword;
	//}
	
    /**
     * 提交退款申请
     * @return
     * @throws Exception
     */
	public Map<String,Object> submitXmlToRefund() throws Exception
	{
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        closeableHttpClient = httpClientBuilder.build();
		// HttpClient
		// if (!hasInit) {
	     //       init();
	     //}

		HttpPost httpPost = new HttpPost(refund_url);
		String xml = getPackage();
        System.out.println("=======================>退款xml生成啦："+xml);
		StringEntity entity;
        String status = null;
		String return_code = null;
		String return_mch_id = null;
		String err_code = null;
		String err_msg = null;
		String return_out_refund_no = null;
        String return_out_trade_no = null;
		String return_refund_id = null;
		String return_refund_channel = null;//ORIGINAL—原路退款 BALANCE—退回到余额
		String return_refund_fee = null;
		Map<String,Object> result_map = new HashMap<String, Object>();
		try
		{
			entity = new StringEntity(xml, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse httpResponse;
			// post请求
			httpResponse = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null)
			{
				// 打印响应内容
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				logger.info(result);
				// 过滤
				result = result.replaceAll("<![CDATA[|]]>", "");
                status = Jsoup.parse(result).select("status").html().toString();
                result_map.put("status",status);
                if(status.equals("0")) {
                        return_code = Jsoup.parse(result).select("result_code").html();
                        return_mch_id = Jsoup.parse(result).select("mch_id").html();
                    if (return_code.equals("0")) {
                        return_out_refund_no = Jsoup.parse(result).select("out_refund_no").html();
                        return_out_trade_no = Jsoup.parse(result).select("out_trade_no").html();
                        return_refund_id = Jsoup.parse(result).select("refund_id").html();
                        return_refund_channel = Jsoup.parse(result).select("refund_channel").html();
                        return_refund_fee = Jsoup.parse(result).select("refund_fee").html();

                        result_map.put("return_refund_fee",return_refund_fee);
                        result_map.put("return_out_trade_no",return_out_trade_no);
                    }else{
                        err_code = Jsoup.parse(result).select("err_code").html();
                        err_msg = Jsoup.parse(result).select("err_msg").html();
                        result_map.put("err_code",err_code);
                        result_map.put("err_msg",err_msg);
                    }
                    result_map.put("return_code",return_code);

                }else{
                    result_map.put("message", Jsoup.parse(result).select("message").html().toString());
                }

			}
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e)
		{
			logger.error("",e);
		}
		return result_map;
	}
	
	//private void init() throws Exception {
	//	//指定读取证书格式为PKCS12
	//	KeyStore keyStore = KeyStore.getInstance("PKCS12");
	//	//读取本机存放的PKCS12证书文件
	//	FileInputStream instream = new FileInputStream(new File(ConfigUtil.get("cert_path")));
	//	try {
	//	//指定PKCS12的密码(商户ID)
	//	keyStore.load(instream, getCertPassword().toCharArray());
	//	} finally {
	//	instream.close();
	//	}
	//	SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, getCertPassword().toCharArray()).build();
	//	//指定TLS版本
	//	SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	//	sslcontext,new String[] { "TLSv1" },null,
	//	SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	//	//设置httpclient的SSLSocketFactory
	//	closeableHttpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	//}
	
	public String getPackage()
	{
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.put("service", this.service);
		treeMap.put("mch_id", this.mch_id);
		treeMap.put("out_trade_no", this.out_trade_no);
		treeMap.put("transaction_id", this.transaction_id);
		treeMap.put("out_refund_no", this.out_refund_no);
		treeMap.put("total_fee", this.total_fee);
		treeMap.put("refund_fee", this.refund_fee);
		treeMap.put("total_fee", this.total_fee);
		treeMap.put("nonce_str", this.nonce_str);
		treeMap.put("refund_channel", this.refund_channel);
		treeMap.put("op_user_id", this.mch_id);

		
		 ArrayList<String> list = new ArrayList<String>();
	        for(Entry<String,Object> entry:treeMap.entrySet()){
	            if(entry.getValue()!=""){
	                list.add(entry.getKey() + "=" + entry.getValue() + "&");
	            }
	        }
	        int size = list.size();
	        String[] arrayToSort = list.toArray(new String[size]);
	        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
	        StringBuilder sb = new StringBuilder();
	        for(int i = 0; i < size; i ++) {
	            sb.append(arrayToSort[i]);
	        }
		
		sb.append("key=" + ConfigUtil.get("key"));
		logger.info(sb.toString());
		sign = MD5Util.MD5Encode(sb.toString(),"utf-8").toUpperCase();
		treeMap.put("sign",sign);
        
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>\n");
		for (Entry<String, Object> entry : treeMap.entrySet())
		{
			if ("body".equals(entry.getKey()) || "sign".equals(entry.getKey()))
			{
				xml.append("<" + entry.getKey() + "><![CDATA[")
						.append(entry.getValue())
						.append("]]></" + entry.getKey() + ">\n");
			} else
			{
				xml.append("<" + entry.getKey() + ">").append(entry.getValue())
						.append("</" + entry.getKey() + ">\n");
			}
		}
		//xml.append("<sign>").append(sign).append("</sign>\n");
		xml.append("</xml>");
		logger.info(xml.toString());
		return xml.toString();
	}


    // 排序,采用冒泡排序法
    public static String sort(String str) {
        char chs[] = str.toCharArray();
        int size = chs.length;
        char temp;
        for (int i = 0; i < size; i++) {
            for (int j = size - 1; j > i; j--) {
                if (chs[j] < chs[j - 1]) {
                    temp = chs[j];
                    chs[j] = chs[j - 1];
                    chs[j - 1] = temp;
                }
            }
        }
        return new String(chs);
    }
	
//	public queryRefundByIds(String refund_id,String out_refund_no,String out_trade_no,String transaction_id){
//		
//	}
	
	public static String getRefund_url() {
		return refund_url;
	}


	public static void setRefund_url(String refund_url) {
		WXRefund.refund_url = refund_url;
	}


	//public String getAppid() {
	//	return appid;
	//}
    //
    //
	//public void setAppid(String appid) {
	//	this.appid = appid;
	//}


	public String getMch_id() {
		return mch_id;
	}


	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}


	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public String getTransaction_id() {
		return transaction_id;
	}


	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}


	public String getOut_trade_no() {
		return out_trade_no;
	}


	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}


	public String getOut_refund_no() {
		return out_refund_no;
	}


	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}


	public int getTotal_fee() {
		return total_fee;
	}


	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}


	public int getRefund_fee() {
		return refund_fee;
	}


	public void setRefund_fee(int refund_fee) {
		this.refund_fee = refund_fee;
	}


	public String getOp_user_id() {
		return op_user_id;
	}


	public void setOp_user_id(String op_user_id) {
		this.op_user_id = op_user_id;
	}


    public String getRefund_channel() {
        return refund_channel;
    }

    public void setRefund_channel(String refund_channel) {
        this.refund_channel = refund_channel;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
