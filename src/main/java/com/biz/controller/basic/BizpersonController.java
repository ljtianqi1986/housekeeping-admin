package com.biz.controller.basic;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TBizPerson;
import com.biz.model.Pmodel.basic.Pbizperson;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.model.Pmodel.basic.PpersonStatistics;
import com.biz.service.api.WxUtilServiceI;
import com.biz.service.basic.BizpersonServiceI;
import com.framework.controller.BaseController;
import com.framework.model.MessageBox;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.FileUtil;
import com.framework.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tomchen on 17/1/6.
 */
@Controller
@RequestMapping("/bizperson")
public class BizpersonController extends BaseController {


    @Autowired
    private BizpersonServiceI bizpersonService;

    @Autowired
    private WxUtilServiceI wxUtilServiceI;


    @RequestMapping("toBizperson")
    public ModelAndView toLog(ModelAndView mv){
        mv.setViewName("basic/biz/bizperson");
        return mv;
    }

    @RequestMapping("showDatas")
    public void showJqGrid(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("userName", request.getParameter("userName"));
        params.put("phone", request.getParameter("phone"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<Pbizperson> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager = bizpersonService.queryBizpersons(pager);

        Paging<Pbizperson> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }

    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response, HttpServletRequest request,Pbizperson pbizperson) {
        MessageBox mb = getBox();
        try {
            if(StringUtil.isNullOrEmpty(pbizperson.getCode())){//add
                System.out.println(pbizperson.getCode());
                TBizPerson tBizPerson = new TBizPerson();
                BeanUtils.copyProperties(pbizperson,tBizPerson);
                tBizPerson.setCreate_time(new Date());
                bizpersonService.save(tBizPerson);
                //获取微信带参二维码ticket
                int couponseMoney =BigDecimal.valueOf(pbizperson.getCouponsMoney()).multiply(new BigDecimal(100)).intValue();
                String ticket=wxUtilServiceI.getQrcodeTicket(tBizPerson.getCode(),couponseMoney);
                if(StringUtils.isBlank(ticket)){
                    mb.setSuccess(false);
                    mb.setMsg("获取微信带参二维码失败，请稍后重试或联系管理员！");
                }else{
                    tBizPerson.setTicket(ticket);
                    bizpersonService.update(tBizPerson);
                }
            }else{//update
                TBizPerson tBizPerson = bizpersonService.getById(pbizperson.getCode());
                BeanUtils.copyProperties(pbizperson,tBizPerson,"code","brand_count","couponsMoney","ticket","isdel","create_time");
                if(tBizPerson.getCouponsMoney()==pbizperson.getCouponsMoney()){
                    bizpersonService.update(tBizPerson);
                }else{
                    //获取微信带参二维码ticket
                    int couponseMoney =BigDecimal.valueOf(pbizperson.getCouponsMoney()).multiply(new BigDecimal(100)).intValue();
                    String ticket=wxUtilServiceI.getQrcodeTicket(pbizperson.getCode(),couponseMoney);
                    if(StringUtils.isBlank(ticket)){
                        mb.setSuccess(false);
                        mb.setMsg("获取微信带参二维码失败，请稍后重试或联系管理员！");
                    }else{
                        tBizPerson.setCouponsMoney(couponseMoney);
                        tBizPerson.setTicket(ticket);
                        bizpersonService.update(tBizPerson);
                    }
                }
            }
        }catch (Exception e){
            mb.setSuccess(false);
            mb.setMsg(e.getMessage());
            e.printStackTrace();
        }

        writeJson(mb,response);
    }

    @RequestMapping("toEdit")
    public ModelAndView toEdit(String code) throws Exception {
        mv.clear();
        mv.addObject("code",code);
        TBizPerson tBizPerson = bizpersonService.getById(code);
        mv.addObject("bizperson",tBizPerson);
        mv.setViewName("basic/biz/biz_detail");
        return mv;
    }

    @RequestMapping("deByIds")
    public void delGridById(HttpServletResponse response, HttpServletRequest request,String ids) throws Exception {
        MessageBox mb = getBox();
        try{
            bizpersonService.executeHql(StringUtil.formateString("update TBizPerson set isdel =1 where code in ({0})",StringUtil.formatSqlIn(ids)));
            mb.setSuccess(true);
        }catch (Exception e){
            mb.setSuccess(false);
        }
        writeJson(mb,response);
    }

    @RequestMapping("showBizPersonForList")
    public void showBizPersonForList(HttpServletResponse response, HttpServletRequest request)throws Exception{
        List<Pbizperson> list=new ArrayList<>();
        try{ list=bizpersonService.showBizPersonForList();}
        catch (Exception e)
        {
            e.printStackTrace();
        }
        writeJson(list, response);
    }


    @RequestMapping("toSumOfBizperson")
    public ModelAndView toSumOfBizperson(ModelAndView mv){
        mv.clear();

        List<Pbizperson> bizList = bizpersonService.getBizList();
        List<Pbrand> pbrandList = bizpersonService.getBrandListWithBizCode();

        String brandString = JSON.toJSONString(pbrandList);

        mv.addObject("bizList",bizList);
        mv.addObject("pbrandList",pbrandList);
        mv.addObject("brandString",brandString);
        mv.setViewName("basic/biz/sumOfBizperson");

        return mv;
    }






    @RequestMapping("personStatistics")
    public void personStatistics(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map map = getParameterByRequest(request);
        Paging paging= bizpersonService.personStatistics(map);

        writeJsonNoReplace(paging, response);
    }



    @RequestMapping("personStatisticsTotal")
    public void personStatisticsTotal(HttpServletResponse response, String json)throws Exception{
        //前端封装的查询参数
        Map map = JSON.parseObject(json, Map.class);
        PpersonStatistics ppersonStatistics= bizpersonService.personStatisticsTotal(map);
        //久零贝核销率
        PpersonStatistics ppersonStatisticsUser = bizpersonService.personStatisticsTotalUser(map);
        if(ppersonStatistics != null)
        {
            if(ppersonStatisticsUser != null && ppersonStatisticsUser.getCoinPayRate()!=null){
                ppersonStatistics.setCoinPayRate(ppersonStatisticsUser.getCoinPayRate());
                ppersonStatistics.setMemberCount(ppersonStatisticsUser.getMemberCount());
            }
            writeJsonNoReplace(ppersonStatistics, response);
        }else{
            writeJsonNoReplace(new PpersonStatistics(), response);
        }


    }



    /**
     * 加载发券增长图表数据
     * @param response
     * @throws Exception
     */
    @RequestMapping("loadCouponSum")
    public void loadCouponSum(HttpServletResponse response,String type,String json) throws Exception{

        Map<String, Object> data = bizpersonService.loadCouponSum(Integer.valueOf(type),json);

        writeJson(data,response);
    }


    /**
     * 加载会员增长图表数据
     * @param response
     * @throws Exception
     */
    @RequestMapping("loadMemberSum")
    public void loadMemberSum(HttpServletResponse response,String type,String json) throws Exception{

        Map<String, Object> data = bizpersonService.loadMemberSum(Integer.valueOf(type),json);

        writeJson(data,response);
    }



    /**
     * 加载pos机增长图表数据
     * @param response
     * @throws Exception
     */
    @RequestMapping("loadPosSum")
    public void loadPosSum(HttpServletResponse response,String type,String json) throws Exception{

        Map<String, Object> data = bizpersonService.loadPosSum(Integer.valueOf(type),json);

        writeJson(data,response);
    }



    /**
     * @param response
     * @throws Exception
     */
    @RequestMapping("excelBrandInfo")
    public void excelBrandInfo(HttpServletResponse response,
                                         String parameters, ServletOutputStream outputStream)
            throws Exception {
        //设置参数
        Map maps = JSON.parseObject(parameters,
                Map.class);
        List<PpersonStatistics> list = bizpersonService.excelBrandInfo(maps);
        String[] fstring = { "商户", "发券笔数", "发券金额", "服务费金额", "服务费率", "领券比率",
                "POS机数量"};

        setExcelBrandInfoStyle(list, response, outputStream, fstring);
    }


    /**
     * 设置导出excel样式
     * @param list
     * @throws Exception
     */
    public void setExcelBrandInfoStyle(List<PpersonStatistics> list,
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
                PpersonStatistics order = list.get(i);
                // 第四步，创建单元格，并设置值
                row.createCell(0).setCellValue(order.getBrandName());
                row.createCell(1).setCellValue(order.getGiveCount());
                row.createCell(2).setCellValue(order.getGiveTotal());
                row.createCell(3).setCellValue(order.getServiceTotal());
                row.createCell(4).setCellValue(order.getServiceRate());
                row.createCell(5).setCellValue(order.getGiveGetScale());
                row.createCell(6).setCellValue(order.getPosCount());
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

}
