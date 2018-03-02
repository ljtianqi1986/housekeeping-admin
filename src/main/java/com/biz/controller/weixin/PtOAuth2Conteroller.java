package com.biz.controller.weixin;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.PBaseUser;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.BaseUser;
import com.biz.model.Pmodel.basic.Base90Detail;
import com.biz.model.Pmodel.weixin.TextMessage;
import com.biz.model.Pmodel.weixin.WeChatInfo;
import com.biz.service.api.ApiUserServiceI;
import com.biz.service.api.BaseUserServiceI;
import com.biz.service.api.WxActivityServiceI;
import com.biz.service.weixin.WeiXinServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.Tools;
import com.framework.utils.URLConectionUtil;
import com.framework.utils.weixin.MessageUtil;
import com.framework.weixn.WXBizMsgCrypt;
import com.framework.weixn.weixConst;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URLDecoder;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;

import java.util.*;

import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.framework.weixn.AesException;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Created by qziwm on 2017/6/14.
 */
@Controller
@RequestMapping("/api/authorization")
public class PtOAuth2Conteroller extends BaseController {
    @Resource(name = "weiXinService")
    private WeiXinServiceI weiXinService;

    @Resource(name = "wxActivityService")
    private WxActivityServiceI wxActivityService;

    /**
     * 推送component_verify_ticket协议
     * 在公众号第三方平台创建审核通过后，微信服务器会向其“授权事件接收URL”每隔10分钟定时推送component_verify_ticket。
     * 第三方平台方在收到ticket推送后也需进行解密（详细请见【消息加解密接入指引】），接收到后必须直接返回字符串success。
     */
    @RequestMapping(value = {"/oauth2_notice"}, method = RequestMethod.POST)
    public void noticePOST(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //logger.error("==POST========推送component_verify_ticket协议=");
        //时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        //消息体签名
        String msg_signature = request.getParameter("msg_signature");
        //logger.error("==timestamp=" + timestamp);
        //logger.error("==nonce=" + nonce);
        //logger.error("==msg_signature=" + msg_signature);
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb = sb.append(line);
        }
        String postData = sb.toString();
        //logger.error("==postData=" + postData);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(postData);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);
            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Encrypt");
            String encrypt = nodelist1.item(0).getTextContent();
//    		logger.error("==encrypt="+encrypt);
            String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
            String fromXML = String.format(format, encrypt);
//    		logger.error("==fromXML="+fromXML);

            WXBizMsgCrypt pc = new WXBizMsgCrypt(weixConst.TOKEN, weixConst.EncodingAESKey, weixConst.APPID);
            String result = pc.decryptMsg(msg_signature, timestamp, nonce, fromXML);
            System.out.println("解密后明文: " + result);

            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(result);
            // 保存消息
            weiXinService.save_component_verify_ticket(requestMap);

            // 响应消息
            PrintWriter out = response.getWriter();
            out.print("success");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("=授权事件接收URL报错====" + e.fillInStackTrace());
            PrintWriter out = response.getWriter();
            out.print("fail");
            out.close();
        }
    }


    /**
     * 发起授权页的体验URL
     */
    @RequestMapping(value = "/oauth2_take")
    public ModelAndView oauth2_take(String appid) throws Exception {
        String preAuthCode = weiXinService.get_pre_auth_code();
        String redirect_uri = ConfigUtil.get("SYS_URL") + "/api/authorization/oauth2_authorCallback.ac?appid=" + appid;//回掉URL

        String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid="
                + weixConst.APPID + "&pre_auth_code=" + preAuthCode + "&redirect_uri=" + redirect_uri;
        mv.addObject("url", url);
        mv.setViewName("weixin/oauth2_take");
        return mv;
    }


    /**
     * 授权微信返回
     */
    @RequestMapping(value = "/oauth2_authorCallback")
    public ModelAndView authorCallback(HttpServletRequest request, HttpServletResponse response) {
        String appid = request.getParameter("appid");
        logger.error("=授权微信返回=appid="+appid);
        //步骤1：第三方平台方获取预授权码（pre_auth_code）
        String auth_code = request.getParameter("auth_code");
        String expires_ins = request.getParameter("expires_in");
        //System.out.println("=授权微信返回=auth_code="+auth_code+"=expires_in="+expires_ins);
        System.out.println("=授权微信返回=appid="+appid);
        if (StringUtils.isNotBlank(appid) && StringUtils.isNotBlank(auth_code)) {
            try {
                //步骤2、使用授权码换取公众号的接口调用凭据和授权信息
                JSONObject jSONObject = weiXinService.api_query_auth(auth_code);
                if (jSONObject != null) {
                    JSONObject jSON = jSONObject.getJSONObject("authorization_info");
                    String authorizer_refresh_token = jSON.getString("authorizer_refresh_token");
                    String authorizer_appid = jSON.getString("authorizer_appid");
                    //判断授权公众号是否是需要授权的公众号
                    if (!authorizer_appid.equals(appid)) {
                        mv.addObject("err_msg", "授权失败，你非该公众号管理员！");
                        mv.setViewName("weixin/oauth2error");
                        return mv;
                    }

                    //写入当前公众号或小程序的接口调用凭据
                    String authorizer_access_token = jSON.getString("authorizer_access_token");
                    int expires_in = 0;
                    //保存
                    if (StringUtils.isNotBlank(authorizer_access_token)) {
                        expires_in = jSON.getInt("expires_in");
                    }
                    /*System.out.println("######################");
                    System.out.println(authorizer_appid);
                    System.out.println(auth_code);
                    System.out.println(authorizer_refresh_token);
                    System.out.println(authorizer_access_token);
                    System.out.println(expires_in);*/

                    //保存，修改 授权记录微信信息
                    weiXinService.save_WeChatInfoByAppid(authorizer_appid, auth_code, authorizer_refresh_token, authorizer_access_token, expires_in);
                }
            } catch (Exception e) {
                logger.error("=授权微信返回=更新代理商出错=", e.fillInStackTrace());
                e.printStackTrace();
            }
        }
        mv.addObject("err_msg", "授权成功！");
        mv.setViewName("weixin/oauth2error");
        return mv;
    }


    /**
     * 公众号消息与事件接收URL
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = {"{APPID}/oauth2_index"}, method = RequestMethod.POST)
    public void WXPost(@PathVariable String APPID, HttpServletRequest request, HttpServletResponse response)
            throws IOException, AesException {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 从请求中读取整个post数据
        InputStream inputStream = request.getInputStream();
        String msg = IOUtils.toString(inputStream, "UTF-8");
        //时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        //消息体签名
        String msg_signature = request.getParameter("msg_signature");
        WXBizMsgCrypt pc = new WXBizMsgCrypt(weixConst.TOKEN, weixConst.EncodingAESKey, weixConst.APPID);
        String result = "";


        //result = "<xml><ToUserName><![CDATA[gh_9ebf08e48568]]></ToUserName><FromUserName><![CDATA[obisKwbgqvUzGWMvzu3esEOA8qxU]]></FromUserName><CreateTime>1500536075</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[unsubscribe]]></Event><EventKey><![CDATA[]]></EventKey></xml>";
        result=pc.decryptMsg(msg_signature, timestamp, nonce, msg);


System.out.println("=公众号消息与事件接收=解密后明文: " + result);
        // 在5秒内返回字符串
        String respMessage = processRequest(result);
        //System.out.println("=返回给微信=" + respMessage);
        if (StringUtils.isBlank(respMessage)) {
            // 响应消息
            PrintWriter out = response.getWriter();
            out.print("success");
            out.close();
        } else {
            //将公众平台回复用户的消息加密打包--encryptMsg
            String rmsg = pc.encryptMsg(respMessage, timestamp, nonce);
            //logger.error("=加密返回="+rmsg);
            // 响应消息
            PrintWriter out = response.getWriter();
            out.print(rmsg);
            out.close();
        }
        // 调用核心业务类接收消息、处理消息
        //processRealRequest(result);

    }


    /**
     * 在5秒内返回字符串
     */
    public String processRequest(String result) {
        String respMessage = "";
        int typeMsg=0;//0：文本消息 1：图文消息
        // 默认返回的文本消息内容
        String respContent = "";
        String open_id = "";
        String toUserName = "";
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(result);
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 发送方帐号（open_id）
            open_id = requestMap.get("FromUserName");
            // 公众帐号
            toUserName = requestMap.get("ToUserName");
            //获取appid
            String appid= weiXinService.toUserNameToAppid(toUserName);

            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                if (eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    // 取消关注,更新关注状态位
                    this.updateUserState(open_id,0);
                } else if (eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    // 关注事件
                    //关注送活动
                    add_gz_Balance_90(open_id,appid);
                    //修改关注者状态，为已关注状态
                    this.updateUserState(open_id, 1);
                    //带参
                    //扫描带参数二维码事件,事件KEY值，qrscene_为前缀，后面为二维码的参数值
                    String eventKey = requestMap.get("EventKey");
                    String open_id_s= requestMap.get("FromUserName");

                    System.out.println("#####################################");
                    System.out.println("eventKey="+eventKey);

                    if(org.apache.commons.lang.StringUtils.isNotBlank(eventKey)&&eventKey.contains("qrscene_")){
                        String scene= eventKey.substring(eventKey.indexOf("_")+1, eventKey.length());
                        if (!scan(open_id_s,scene,appid)) {
                            respContent = "fail";
                        }
                }

                    typeMsg=1;//发送图文消息
                    respContent = GZ_msg(open_id,toUserName);

                    System.out.println("############buffer###########:" + respContent);
                //    textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);


                }else if (eventType
                        .equalsIgnoreCase(MessageUtil.EVENT_TYPE_ALREADY_SUBSCRIBE)) {
                    //带参数二维码
                    //带参数二维码
                    String open_id_s = requestMap.get("FromUserName");
                    String eventKey_s = requestMap.get("EventKey");
                    if (!scan(open_id_s,eventKey_s,appid)) {
                        respContent = "fail";
                    }
                } else {
                    respContent = "";
                }
            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                // 文本消息
            }
        } catch (Exception e) {
            logger.error("==调用核心业务类接收消息、处理消息=报错=", e.fillInStackTrace());
            e.printStackTrace();
        }

        //不处理
        if (StringUtils.isBlank(respContent)) {
            return "";
        }
        if(typeMsg==1){
            //图文消息 ，关注消息
            respMessage=respContent;
        }else{
                // 回复文本消息
                TextMessage textMessage = new TextMessage();
                textMessage.setToUserName(open_id);
                textMessage.setFromUserName(toUserName);
                textMessage.setCreateTime(new Date().getTime());
                textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
                textMessage.setFuncFlag(0);
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
        }
        return respMessage;
    }


    /**
     * 调用核心业务类接收消息、处理消息（异步处理）
     *
     * @param result
     */
    public void processRealRequest(String result) {
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(result);
            // 发送方帐号（open_id）
            String open_id = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

        } catch (Exception e) {
            logger.error("==调用核心业务类接收消息、处理消息=报错=", e.fillInStackTrace());
            e.printStackTrace();
        }
    }

    private void updateUserState(String open_id,int state) {
        Map<String, Object> user = new HashMap<>();
        user.put("open_id", open_id);
        user.put("state",state);
        try {
            weiXinService.updateUserState(user);
            //dao.save("UserInfoDao.updateCliUserState", user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void add_gz_Balance_90(String openid,String appid) {
        try {
            Map<String,String> map1=new HashMap<>();
            map1.put("open_id",openid);
            map1.put("sceneType","1");
           String msg=wxActivityService.adActivityListDate(openid,"1",appid);
            System.out.println(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描带参数二维码事件,用户已关注时的事件推送
     */
    private synchronized boolean scan(String open_id,String eventKey,String appid) {
        boolean pd = false;
        try {
            Map<String, String> map1 = new HashMap<>();
            map1.put("open_id", open_id);
            map1.put("eventKey", eventKey);
            System.out.println("#####open_id:"+open_id+",eventKey:"+eventKey);
            String msg = wxActivityService.doWxscan(open_id,eventKey,appid);
                if ("true".equals(msg)) {
                    pd = true;
                    System.out.println("成功");
                }
        } catch (Exception e) {
            System.out.println("=扫描带参数二维码=报错=" + e.fillInStackTrace());
        } finally {
            return pd;
        }
    }

    //关注发送消息
    private String GZ_msg(String open_id,String toUserName)throws Exception{
        String msg="";
        //json格式
        //msg=messageJson(open_id);
        //xml格式
        msg=messageXml(open_id,toUserName);
        return  msg;
    }

    /**
     * 生成json格式的图文消息
     * @param
     * @return
     * @throws Exception
     */
    //@RequestMapping(value="/messageJson")
    public String messageJson(String openId)throws Exception{
        Map<String,Object> resultMap=wxActivityService.messageJson(openId);
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        return jsonObject.toString();
    }

    /**
     * 生成xml格式的图文消息
     * @param openId
     * @param appId
     * @throws Exception
     */
    public String messageXml(String openId,String appId)throws Exception{
        String message=wxActivityService.messageXml(openId,appId);
        return message;
    }
}
