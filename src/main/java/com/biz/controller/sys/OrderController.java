package com.biz.controller.sys;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.PorderMain;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.PorderSend;
import com.biz.model.Pmodel.basic.Ppics;
import com.biz.model.Pmodel.basic.PunionSumInfo;
import com.biz.service.api.WxTemplateServiceI;
import com.biz.service.base.OrderServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.utils.FileUtil;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzq on 2017/1/6.
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Resource(name = "orderService")
    private OrderServiceI orderService;

    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;

    //样例
//    @Reference
//    private com.jswzc.api.service.wxTemplate.WxTemplateServiceI wxTemplateServiceJswzc;

    /**
     * 跳转订单列表
     * @return
     */
    @RequestMapping("/toOrder")
    public ModelAndView toOrder(){
        ModelAndView mv = new ModelAndView();

        mv.setViewName("sys/order");
        return mv;
    }
    /**
     * 跳转联盟专区订单列表
     * @return
     */
    @RequestMapping("/toOrderUnion")
    public ModelAndView toOrderUnion(){
        ModelAndView mv = new ModelAndView();

        mv.setViewName("sys/orderUnion");
        return mv;
    }

    /**
     * 加载订单列表
     * @return
     */
    @RequestMapping("/showOrders")
    public @ResponseBody void showOrders(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map map = getParameterByRequest(request);
        //从session中获取shopId
        User user =  (User)getShiroAttribute("user");
        String shopId = user.getMapId();

        map.put("shopId",shopId);
        Paging paging= orderService.queryOrders(map);

        writeJson(paging, response);
    }
    /**
     * 加载联盟专区订单列表
     * @return
     */
    @RequestMapping("/showOrderUnions")
    public void showOrderUnions(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map map = getParameterByRequest(request);
        //从session中获取shopId
        User user =  (User)getShiroAttribute("user");
        String shopId = user.getMapId();
        if(user.getIdentity()==2){  //代理商登录
            map.put("agentCode",user.getIdentity_code());
        }else if(user.getIdentity()==3){  //商户登录
            map.put("brandCode",user.getIdentity_code());
        }
        Paging paging= orderService.queryOrderUnions(map);

        writeJson(paging, response);
    }

    /**
     * 根据订单id获取订单信息
     * @return
     */
    @RequestMapping("toDetail")
    public ModelAndView toDetail(String id,String detailId)
            throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("id", id);
        mv.addObject("detailId",detailId);
        mv.setViewName("sys/orderDetail");

        return mv;
    }


    /**
     * 关闭订单
     *
     * @param response
     * @param id
     * @throws Exception
     */
    @RequestMapping("closeOrder")
    public void closeOrder(HttpServletResponse response, String id)
            throws Exception {
        Map map = new HashMap();
        try {
            User user =  (User)getShiroAttribute("user");
            map = orderService.closeOrder(id,user.getId());
        } catch (Exception e) {
            map.put("state", "0");
            map.put("msg", "关闭失败");
        }
        writeJson(map, response);
    }

    /**
     * 加载订单信息
     *
     * @param response
     * @param id
     * @throws Exception
     */
    @RequestMapping("findOrderInfoById")
    public void findOrderInfoById(HttpServletResponse response, String id,String detailId)
            throws Exception {
        Map<String, Object> map = orderService.findOrderInfoById(id,detailId);
        writeJson(map, response);
    }


    /**
     * 导出excelMain
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportExcel_shopDeMain")
    public void exportExcel_shopDeMain(HttpServletResponse response,
                                       String parameters, ServletOutputStream outputStream)
            throws Exception {
        parameters = URLDecoder.decode(parameters, "utf-8");
        Map<String, Map<String, Object>> maps = JSON.parseObject(parameters,
                Map.class);
        List<PorderMain> list = orderService.exportExcel_shopDeMain(maps);
        String[] fstring = { "订单编号", "支付单号", "购买账号", "购买人", "商品总额", "运费金额",
                "实付金额", "优惠金额", "积分抵扣金额", "支付状态", "支付途径", "收货地址", "下单时间",
                "确认收货时间", "特殊要求", "贺卡留言" };

        setExcelStyle_shopDeMain(list, response, outputStream, fstring);
    }


    /**
     * 导出excelDetail
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportExcel_shopDeDetail")
    public void exportExcel_shopDeDetail(HttpServletResponse response,
                                         String parameters, ServletOutputStream outputStream)
            throws Exception {
        parameters = URLDecoder.decode(parameters, "utf-8");
        //设置参数
        Map<String, Map<String, Object>> maps = JSON.parseObject(parameters,
                Map.class);
        List<PorderMain> list = orderService.exportExcel_shopDeMain(maps);
        String[] fstring = { "订单编号", "支付单号", "购买账号", "购买人", "商品信息", "购买数量",
                "购买单价", "商品总额", "运费金额", "实付金额", "优惠金额", "积分抵扣金额", "支付状态",
                "支付途径", "订单状态", "收货地址", "下单时间", "确认收货时间", "买家留言", "特殊要求",
                "贺卡留言" };

        setExcelStyle_shopDeDetail(list, response, outputStream, fstring);
    }

    /**
     * 导出excelDetail
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportExcel_orderUnion")
    public void exportExcel_orderUnion(HttpServletResponse response,
                                         String parameters, ServletOutputStream outputStream)
            throws Exception {
        parameters = URLDecoder.decode(parameters, "utf-8");
        //设置参数
        Map<String, Map<String, Object>> maps = JSON.parseObject(parameters,
                Map.class);
        List<PorderMain> list = orderService.exportExcel_orderUnion(maps);
        String[] fstring = { "订单编号", "支付单号", "购买账号", "购买人", "商品信息", "购买数量",
                "购买单价", "商品总额", "运费金额", "实付金额", "久零贝金额", "支付状态",
                "支付途径", "订单状态", "收货地址", "下单时间", "确认收货时间", "买家留言" };

        setExportExcel_orderUnion(list, response, outputStream, fstring);
    }

    private void setExportExcel_orderUnion(List<PorderMain> list, HttpServletResponse response, ServletOutputStream outputStream, String[] fstring) {

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        for (int j = 0; j <= list.size() / 60000; j++) {
            HSSFSheet sheet = wb.createSheet("sheet" + j);
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

            if (fstring.length > 0) {
                for (int k = 0; k < fstring.length; k++) {
                    HSSFCell cell = row.createCell(k);
                    cell.setCellValue(fstring[k]);
                    cell.setCellStyle(style);
                }
            }

            // 第五步，写入实体数据
            for (int i = j * 60000, k = 0; i < ((j + 1) * 60000 < list.size() ? (j + 1) * 60000
                    : list.size()); i++) {
                row = sheet.createRow(k + 1);
                k++;
                PorderMain order = list.get(i);
                // 第四步，创建单元格，并设置值
                // "订单编号", "支付单号", "购买账号", "购买人", "商品信息", "购买数量",
                //"购买单价", "商品总额", "运费金额", "实付金额", "久零贝金额", "支付状态",
                    //    "支付途径", "订单状态", "收货地址", "下单时间", "确认收货时间", "买家留言"
                row.createCell(0).setCellValue(order.getCode());
                row.createCell(1).setCellValue(order.getPayCode());
                row.createCell(2).setCellValue(order.getPhone());
                row.createCell(3).setCellValue(order.getBuyUserName());
                row.createCell(4).setCellValue(order.getGoodsName()+"("+order.getTypesNames()+")");
                row.createCell(5).setCellValue(order.getGoodsCount());
                row.createCell(6).setCellValue(order.getGoodsPrice());
                row.createCell(7).setCellValue(order.getGoodsTotal());
                row.createCell(8).setCellValue(order.getFreightTotal());
                row.createCell(9).setCellValue(order.getPayTotal());
                row.createCell(10).setCellValue(order.getCoinPayTotal());
                if("1".equals(order.getState())){
                    row.createCell(11).setCellValue("已支付");
                }
                else
                { row.createCell(11).setCellValue("未支付");}
                row.createCell(12).setCellValue("久零贝支付");
                switch (order.getDetailState())
                {
                    case "0":row.createCell(13).setCellValue("待付款");break;
                    case "1":row.createCell(13).setCellValue("已付款");break;
                    case "2":row.createCell(13).setCellValue("待收货");break;
                    case "3":row.createCell(13).setCellValue("待评价");break;
                    case "4":row.createCell(13).setCellValue("完成");break;
                    case "5":row.createCell(13).setCellValue("退款中");break;
                    case "6":row.createCell(13).setCellValue("已退款");break;
                    case "7":row.createCell(13).setCellValue("待备货");break;
                    case "9":row.createCell(13).setCellValue("未备货退款");break;
                }
                row.createCell(14).setCellValue(order.getBuyAddr());
                row.createCell(15).setCellValue(order.getCreateTime());
                row.createCell(16).setCellValue(order.getConfirmTime());
                row.createCell(17).setCellValue(order.getMessage());

            }
        }
        // 第六步，将文件存到指定位置
        try {
            String serverPath = Global.getConfig("DOWNLOADURL");
            String randomfile = FileUtil.getListNum(FileUtil
                    .getRandomString2(5));
            serverPath = serverPath + "/" + randomfile + ".xls";
            FileOutputStream fout = new FileOutputStream(serverPath);
            wb.write(fout);
            fout.close();
            download(serverPath, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置导出excel样式
     * @param list
     * @throws Exception
     */
    public void setExcelStyle_shopDeMain(List<PorderMain> list,
                                         HttpServletResponse response, ServletOutputStream outputStream,
                                         String[] fstring) throws Exception {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        for (int j = 0; j <= list.size() / 60000; j++) {
            HSSFSheet sheet = wb.createSheet("sheet" + j);
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

            if (fstring.length > 0) {
                for (int k = 0; k < fstring.length; k++) {
                    HSSFCell cell = row.createCell(k);
                    cell.setCellValue(fstring[k]);
                    cell.setCellStyle(style);
                }
            }

            // 第五步，写入实体数据
            for (int i = j * 60000, k = 0; i < ((j + 1) * 60000 < list.size() ? (j + 1) * 60000
                    : list.size()); i++) {
                row = sheet.createRow(k + 1);
                k++;
                PorderMain order = list.get(i);
                // 第四步，创建单元格，并设置值
                row.createCell(0).setCellValue(order.getCode());
                row.createCell(1).setCellValue(order.getPayCode());
                row.createCell(2).setCellValue(order.getLoginName());
                row.createCell(3).setCellValue(order.getBuyUserName());
                row.createCell(4).setCellValue(order.getGoodsTotal());
                row.createCell(5).setCellValue(order.getFreightTotal());
                row.createCell(6).setCellValue(order.getPayTotal());
                row.createCell(7).setCellValue(order.getCouponTotal());
                row.createCell(8).setCellValue(order.getIntegralTotal());
                if (order.getState().equals("0")) {
                    row.createCell(9).setCellValue("未支付");
                } else {
                    row.createCell(9).setCellValue("已支付");
                }
                if (order.getPaymentMethod()!=null&&order.getPaymentMethod().equals("1")) {
                    row.createCell(10).setCellValue("货到付款");
                }

                switch (order.getPaymentRoute()) {
                    case "0":
                        row.createCell(10).setCellValue("余额支付");
                        break;
                    case "1":
                        row.createCell(10).setCellValue("银联支付");
                        break;
                    case "2":
                        row.createCell(10).setCellValue("支付宝支付");
                        break;
                    case "3":
                        row.createCell(10).setCellValue("微信支付");
                        break;
                    case "4":
                        row.createCell(10).setCellValue("其他支付");
                        break;
                    default:
                        break;
                }

                row.createCell(11).setCellValue(
                        order.getPro() + order.getCity() + order.getArea()
                                + order.getBuyAddr());
                row.createCell(12).setCellValue(order.getCreateTime());
                row.createCell(13).setCellValue(order.getConfirmTime());
                if (order.getGiveType() != null
                        && order.getGiveType().equals("0")) {
                    row.createCell(14).setCellValue(
                            "送自己,特殊要求:" + order.getGiveMsg());
                } else {
                    row.createCell(14).setCellValue(
                            "送他人,手机号:" + order.getGivePhone() + ",特殊要求:"
                                    + order.getGiveMsg());
                }
                row.createCell(15).setCellValue(order.getMessage());
            }
        }
        // 第六步，将文件存到指定位置
        try {
            String serverPath = Global.getConfig("DOWNLOADURL");
            String randomfile = FileUtil.getListNum(FileUtil
                    .getRandomString2(5));
            serverPath = serverPath + "/" + randomfile + ".xls";
            FileOutputStream fout = new FileOutputStream(serverPath);
            wb.write(fout);
            fout.close();
            download(serverPath, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置导出excel样式
     * @param list
     * @throws Exception
     */
    public void setExcelStyle_shopDeDetail(List<PorderMain> list,
                                           HttpServletResponse response, ServletOutputStream outputStream,
                                           String[] fstring) throws Exception {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        for (int j = 0; j <= list.size() / 60000; j++) {
            HSSFSheet sheet = wb.createSheet("sheet" + j);
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

            if (fstring.length > 0) {
                for (int k = 0; k < fstring.length; k++) {
                    HSSFCell cell = row.createCell(k);
                    cell.setCellValue(fstring[k]);
                    cell.setCellStyle(style);
                }
            }

            // 第五步，写入实体数据
            for (int i = j * 60000, k = 0; i < ((j + 1) * 60000 < list.size() ? (j + 1) * 60000
                    : list.size()); i++) {
                row = sheet.createRow(k + 1);
                k++;
                PorderMain order = list.get(i);
                // 第四步，创建单元格，并设置值
                row.createCell(0).setCellValue(order.getCode());
                row.createCell(1).setCellValue(order.getPayCode());
                row.createCell(2).setCellValue(order.getLoginName());
                row.createCell(3).setCellValue(order.getBuyUserName());
                row.createCell(4).setCellValue(
                        order.getGoodsName() + "(" + order.getTypesNames()
                                + ")");
                row.createCell(5).setCellValue(order.getGoodsCount());
                row.createCell(6).setCellValue(order.getGoodsPrice());
                row.createCell(7).setCellValue(order.getGoodsTotal());
                row.createCell(8).setCellValue(order.getFreightTotal());
                row.createCell(9).setCellValue(order.getPayTotal());
                row.createCell(10).setCellValue(order.getCouponTotal());
                row.createCell(11).setCellValue(order.getIntegralTotal());
                if (order.getState().equals("0")) {
                    row.createCell(12).setCellValue("未支付");
                } else {
                    row.createCell(12).setCellValue("已支付");
                }
                if (order.getPaymentMethod()!=null&&order.getPaymentMethod().equals("1")) {
                    row.createCell(13).setCellValue("货到付款");
                }

                switch (order.getPaymentRoute()) {
                    case "0":
                        row.createCell(13).setCellValue("余额支付");
                        break;
                    case "1":
                        row.createCell(13).setCellValue("银联支付");
                        break;
                    case "2":
                        row.createCell(13).setCellValue("支付宝支付");
                        break;
                    case "3":
                        row.createCell(13).setCellValue("微信支付");
                        break;
                    case "4":
                        row.createCell(13).setCellValue("其他支付");
                        break;
                    default:
                        break;
                }

                switch (order.getDetailState()) {
                    case "0":
                        row.createCell(14).setCellValue("待付款");
                        break;
                    case "1":
                        row.createCell(14).setCellValue("待发货");
                        break;
                    case "2":
                        row.createCell(14).setCellValue("待收货");
                        break;
                    case "3":
                        row.createCell(14).setCellValue("待评价");
                        break;
                    case "4":
                        row.createCell(14).setCellValue("完成");
                        break;
                    case "5":
                        row.createCell(14).setCellValue("退款中");
                        break;
                    case "6":
                        row.createCell(14).setCellValue("已退款");
                        break;
                    case "7":
                        row.createCell(14).setCellValue("待备货");
                        break;
                    case "8":
                        row.createCell(14).setCellValue("未备货退款");
                        break;
                    default:
                        break;
                }

                row.createCell(15).setCellValue(
                        order.getPro() + order.getCity() + order.getArea()
                                + order.getBuyAddr());
                row.createCell(16).setCellValue(order.getCreateTime());
                row.createCell(17).setCellValue(order.getConfirmTime());
                row.createCell(18).setCellValue(order.getBuyNotes());
                if (order.getGiveType() != null
                        && order.getGiveType().equals("0")) {
                    row.createCell(19).setCellValue(
                            "送自己,特殊要求:" + order.getGiveMsg());
                } else {
                    row.createCell(19).setCellValue(
                            "送他人,手机号:" + order.getGivePhone() + ",特殊要求:"
                                    + order.getGiveMsg());
                }
                row.createCell(20).setCellValue(order.getMessage());
            }
        }
        // 第六步，将文件存到指定位置
        try {
            String serverPath = Global.getConfig("DOWNLOADURL");
            String randomfile = FileUtil.getListNum(FileUtil
                    .getRandomString2(5));
            serverPath = serverPath + "/" + randomfile + ".xls";
            FileOutputStream fout = new FileOutputStream(serverPath);
            wb.write(fout);
            fout.close();
            download(serverPath, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // response.setContentType("application/x-excel");
            response.setContentType("application/binary;charset=utf-8");
            response.setHeader("Content-disposition", "attachment; filename="
                    + filename);// 组装附件名称和格式
            int len = 0;
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            OutputStream out = new BufferedOutputStream(
                    response.getOutputStream());
            byte[] buffer = new byte[5120];
            while ((len = fis.read(buffer)) > 0) // 切忌这后面不能加 分号 ”;“
            {
                out.write(buffer, 0, len);// 向客户端输出，实际是把数据存放在response中，然后web服务器再去response中读取
            }
            fis.close();
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 发货
     * @param orderid
     * @param response
     * @throws Exception
     */
    @RequestMapping("orderSendList")
    public ModelAndView orderSendList(String orderid,String detailId, String message, HttpServletResponse response)
            throws Exception {
        ModelAndView mv = new ModelAndView();
        List<PorderSend> boList = orderService.orderSend(orderid);
        message = URLDecoder.decode(message, "UTF-8");//解码
        mv.addObject("mainId",orderid);
        mv.addObject("detailId",detailId);
        mv.addObject("boList",boList.get(0));
        mv.addObject("message", message);
        mv.setViewName("sys/orderDeliver");

        return mv;
    }




    /**
     * 保存发货信息，并且发货
     *
     * @param orderid
     * @param sendType
     * @param logisticsCompany
     * @param sendCode
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/saveOrderSendANDOrderStateFH", method = RequestMethod.POST)
    public void saveOrderSendANDOrderStateFH(String orderid, String detailId,int sendType,
                                             String logisticsCompany, String sendCode,
                                             HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String res = "fail";
        //填写配送信息
        boolean bo = orderService.saveOrderSend(orderid, sendType,
                logisticsCompany, sendCode);
        if (bo) {
            try{
            //改变状态
            boolean bo2 = orderService.updateBoolOrderState(orderid, 2);
            //发送模板消息
            String app_id=Global.getAppId();
            // String accessToken=wxUtilService.getAccessToken();
            String accessToken="h1IPmzEAeJuioNyANujS0yXpRVR40lpZzmu1RTz6n2VQjv0pIQwkpUSnLAo6ZrfgORnBUhPjC6IByJPO90oUYuGGg3q0RAo-e5EI7Q_yiCCAJJqAzrss9Lm1hSL0eQoWALVjAFAFQA";
            String open_id=orderService.findOpenId(orderid);
            String template_id_short="OPENTM201605400";
            String link_url="";
            PorderSend porderSend=orderService.findPorderSend(orderid);
            String content= "{"+
                    "\"first\": {"+
                    "\"value\":\"您的商品已发货，请等待签收\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"keyword1\": {"+
                    "\"value\":\""+porderSend.getName()+"\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"keyword2\": {"+
                    "\"value\":\""+porderSend.getSendCode()+"\","+
                    "\"color\":\"#000000\""+
                    "},";
            if(porderSend.getSendType().equals("0")){
                content+="\"keyword3\": {"+
                        "\"value\":\"自己配送\","+
                        "\"color\":\"#000000\""+
                        "},";
            }else{
                content+="\"keyword3\": {"+
                        "\"value\":\""+porderSend.getLogisticsCompany()+"\","+
                        "\"color\":\"#000000\""+
                        "},";
            }
            content+="\"remark\": {"+
                    "\"value\":\"您的商品已经发货，大概3-5天到货，请注意查收\","+
                    "\"color\":\"#000000\""+
                    "}}";
//            wxTemplateServiceJswzc.sendTemplate(app_id,accessToken,open_id,template_id_short,link_url,content);
            if (bo2) {
                res = "success";
            }
            }catch (RuntimeException e) {
                res=e.getMessage();
            }

        }

        map.put("res", res);
        writeJson(map, response);
    }

    /**
     * 跳转订单售后列表
     * @return
     */
    @RequestMapping("/toRefundOrder")
    public ModelAndView toRefundOrder(){
        ModelAndView mv = new ModelAndView();

        mv.setViewName("sys/refundOrder");
        return mv;
    }

    /**
     * 加载订单列表
     * @return
     */
    @RequestMapping("/showRefundOrders")
    public @ResponseBody void showRefundOrders(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map map = getParameterByRequest(request);
        //从session中获取shopId
        User user =  (User)getShiroAttribute("user");
        String shopId = user.getMapId();

        map.put("shopId",shopId);
        Paging paging= orderService.queryRefundOrders(map);

        writeJson(paging, response);
    }


    /**
     * 根据订单id获取订单信息
     * @return
     */
    @RequestMapping("toRefundDetail")
    public ModelAndView toRefundDetail(String id,String detailId,String type) throws Exception {
        ModelAndView mv = new ModelAndView();

        mv.addObject("id", id);
        mv.addObject("detailId",detailId);
        mv.addObject("type",type);

        mv.setViewName("sys/refundOrderDetail");
        return mv;
    }

    /**
     * 申请退款退回审批（老方法，包兴业银行）
     * @param id
     * @param state
     * @param remarks
     * @param response
     * @throws Exception
     */
    /*@RequestMapping(value = "/orderRefundSp", method = RequestMethod.POST)
    public void orderRefundSp(final String id,String state,String remarks, final String orderid, final String orderstate,final int paymentRoute,HttpServletResponse response) throws Exception {
        User user =  (User)getShiroAttribute("user");
        final String userId = user.getId();

        int orderidstate=0;//订单状态
        if(state.equals("2"))//通过
        {
            *//*Thread t = new Thread(new Runnable(){
                public void run(){*//*
                    //Thread thisThread = Thread.currentThread();
                    try {
                        Map<String,Object> result;
                        result = orderService.submitToRefund(orderid,userId,id,paymentRoute);
                        System.out.println("退款返回============================>"+result);
                        if(!result.get("status").toString().equals("0")){
                            int befstate;
                            if(orderstate.equals("1") || orderstate.equals("7"))
                                befstate=1;//待发货
                            else
                                befstate=2;//待收货

                            orderService.updateStates(orderid,befstate,id,"5",userId,"退款失败"+result.get("errormsg"));
                        }else{
                            orderidstate=5;//已退款

                        }
                        //thisThread.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
             *//*   }});
            t.start();*//*

        }
        else if(state.equals("3"))//退回，更具申请时的状态改变
        {
            // 1:待发货2:待收货3:待评价4:完成5:退款中6:已退款7:待备货8:未备货退款
            if(orderstate.equals("1") || orderstate.equals("7"))
            {
                //备货前退款
                orderidstate=1;//待发货
            }
            else
            {
                //备货后退款
                orderidstate=2;//待收货
            }

        }
        boolean r= orderService.updateStates(orderid, orderidstate, id, state, userId, remarks);
       // 发送 模板消息
        wxTemplateService.sendOrderRefundDelayInfo(orderid, remarks, state);
        writeJson((r?"success":"订单状态修改失败"), response);

    }*/


    /**
     * 申请退款退回审批（新方法，直接退贝）
     * @param id
     * @param state
     * @param remarks
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/orderRefundSp", method = RequestMethod.POST)
    public void orderRefundSp(final String id,String state,String remarks, final String orderid, final String orderstate,final int paymentRoute,HttpServletResponse response) throws Exception {
        User user =  (User)getShiroAttribute("user");
        final String userId = user.getId();

        int orderidstate=0;//订单状态
        boolean r=false;
        try{

            if(state.equals("2"))//通过
            {
                Map<String,Object> result= orderService.updatesubmitToRefund(orderid,userId,id,paymentRoute,remarks);
                if(result.get("status").toString().equals("0")){
                    //成功

                }else{
                    //失败
                }
            }
            else if(state.equals("3"))//退回，更具申请时的状态改变
            {
                // 1:待发货2:待收货3:待评价4:完成5:退款中6:已退款7:待备货8:未备货退款
                if(orderstate.equals("1") || orderstate.equals("7"))
                {
                    //备货前退款
                    orderidstate=1;//待发货
                }
                else
                {
                    //备货后退款
                    orderidstate=2;//待收货
                }
                orderService.updateStates(orderid, orderidstate, id, state, userId, remarks);
            }
            r=true;
            // 发送 模板消息
            wxTemplateService.sendOrderRefundDelayInfo(orderid, remarks, state);
        }catch (Exception e){
            r=false;
        }finally {

            writeJson((r?"success":"订单状态修改失败"), response);
        }




    }



    /**
     * 申请退款信息的具体内容
     * @param orderId
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/orderRefundObject", method = RequestMethod.POST)
    public void orderRefundObject(String orderId,HttpServletResponse response) throws Exception {
        Map<String, Object> maptmp = orderService.orderRefund(orderId);
        List<Ppics> piclist= orderService.orderRefundPic(orderId);

        Map<String, Object> map=new HashMap<>();
        map.put("orderRefund", maptmp);
        map.put("piclist", piclist);

        writeJson(map, response);
    }



    /**
     * 加载联盟订单统计
     * @return
     */
    @RequestMapping("/loadUnionSumInfo")
    public @ResponseBody void loadUnionSumInfo(
            HttpServletResponse response,String startDate, String endDate, String code)throws Exception{
        Map map = new HashMap();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("code",code);
        User user =  (User)getShiroAttribute("user");
        if(user.getIdentity()==2){  //代理商登录
            map.put("agentCode",user.getIdentity_code());
        }else if(user.getIdentity()==3){  //商户登录
            map.put("brandCode",user.getIdentity_code());
        }
        PunionSumInfo punionSumInfo = orderService.loadUnionSumInfo(map);

        writeJson(punionSumInfo, response);
    }


}
