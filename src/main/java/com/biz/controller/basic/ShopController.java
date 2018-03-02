package com.biz.controller.basic;

import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.*;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.api.WxQrCodeServiceI;
import com.biz.service.basic.ShopServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.utils.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户设置相关
 * Created by liujiajia on 2016/11/14.
 */
@Controller
@RequestMapping("/shop")
public class ShopController extends BaseController{

    @Autowired
    private ShopServiceI shopService;
    @Autowired
    private WxQrCodeServiceI wxQrCodeService;
    @InitBinder("pshop")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pshop.");
    }

    @InitBinder("category")
    public void initBinderFormBean2(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("category.");
    }
    /**
     * 跳转商户设置页面
     * @param mv
     * @return
     */
    @RequestMapping("toShop")
    public ModelAndView toShop(ModelAndView mv){
        mv.setViewName("basic/shop/shop");
        return mv;
    }

    /**
     * 跳转编辑界面
     * @param mv
     * @return
     */
    @RequestMapping("toShopEdit")
    public ModelAndView toShopEdit(ModelAndView mv){
        String identity_code=(String)getShiroAttribute("identity_code");
//        String identity_code="22e48e4c7694479592b17856180d25b7";
        mv.addObject("identity_code",identity_code);
        mv.addObject("type","0");
        mv.setViewName("basic/shop/shop_edit");
        return mv;
    }

    /**
     * 跳转编辑界面
     * @param mv
     * @return
     */
    @RequestMapping("toEdit")
    public ModelAndView toEdit(ModelAndView mv,String sid)throws Exception{
        mv.addObject("sid",sid);
        Pshop pshop=shopService.toGetEdit(sid);
        String category_first=pshop.getCategory_first();
        String category_second=pshop.getCategory_second();
        mv.addObject("shop",pshop);
        mv.addObject("category_first",category_first);
        mv.addObject("category_second",category_second);
        mv.addObject("type","1");
        mv.setViewName("basic/shop/shop_edit");
        return mv;
    }

    @RequestMapping("showShop")
    public void showShop (HttpServletResponse response, HttpServletRequest request)throws Exception{
        //必要的分页参数
        String identity_code=(String)getShiroAttribute("identity_code");
        User user = (User)getShiroAttribute("user");
        int identity =user.getIdentity();
        Map map = getParameterByRequest(request);
        if(identity==3){
            map.put("brand_select",identity_code);
        }else if(identity==2){
            map.put("agent_select",identity_code);
        }



        Paging paging= shopService.showShop(map);

        writeJson(paging, response);
    }

    /**
     * 查询第一级类目
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/queryCategoryFirst")
    public void queryCategoryFirst(HttpServletResponse response) throws Exception
    {
        List<Category> list=shopService.queryCategoryFirst();
       writeJson(list,response);
    }

    /**
     * 查询次级类目
     * @param parent_code
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/queryCategoryByParent")
    public void queryCategoryByParent(String parent_code,HttpServletResponse response) throws Exception
    {
        List<Category> list=shopService.queryCategoryByParent(parent_code);
        writeJson(list,response);
    }


    /**
     * 查询所属商圈
     * @param area_code
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/queryDistrictByPcode")
    public void queryDistrictByPcode(String area_code,HttpServletResponse response) throws Exception
    {
        List<Pdistrict> list=shopService.queryDistrictByPcode(area_code);
        writeJson(list,response);
    }

    /**
     * 查询商户
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/loadBrand")
    public void loadBrand(HttpServletResponse response) throws Exception
    {
        User user= (User) getShiroAttribute("user");
        Map<String,String> map = new HashMap<>();
        if(3==user.getIdentity())
        {
            map.put("identity_code",user.getIdentity_code());
        }
        List<Brand> list=shopService.loadBrandByParm(map);
        writeJson(list,response);
    }


    /**
     *保存门店信息
     * @param response
     * @param request
     * @param pshop
     * @throws Exception
     */
    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response, HttpServletRequest request,Pshop pshop) throws Exception {
        String msg="";
            msg=shopService.doSave(pshop);
        writeJson(msg,response);
    }

    @RequestMapping("toShopAccount")
    public ModelAndView toShopAccount(ModelAndView mv,String sid){
//        String identity_code=(String)getShiroAttribute("identity_code");
        mv.addObject("sid",sid);
        mv.setViewName("basic/shop/shop_account");
        return mv;
    }
    /**
     * 加载门店商品关联
     * @param sid
     * @throws Exception
     */
    @RequestMapping("toShopGoods")
    public ModelAndView toShopGoods(ModelAndView mv,String sid){
        mv.addObject("sid",sid);
        mv.setViewName("basic/shop/shop_goods");
        return mv;
    }
    /**
     * 加载门店用户
     * @param response
     * @param request
     * @param sid
     * @throws Exception
     */
    @RequestMapping("showShopAccount")
    public void showShopAccount (HttpServletResponse response, HttpServletRequest request,String sid)throws Exception{
        //必要的分页参数
        Map map = getParameterByRequest(request);
        map.put("sid",sid);
        Paging paging= shopService.showShopAccount(map);

        writeJson(paging, response);
    }

    /**
     * 修改门店密码
     * @param response
     * @param request
     * @param user_code
     * @throws Exception
     */
    @RequestMapping("updatePwd")
    public void updatePwd(HttpServletResponse response, HttpServletRequest request,String user_code,String newPwd) throws Exception {
        String msg="";
        msg=shopService.updatePwd(user_code,newPwd);
        writeJson(msg,response);
    }

    /**
     * 修改门店手机号
     * @param response
     * @param request
     * @param user_code
     * @throws Exception
     */
    @RequestMapping("updatePhone")
    public void updatePhone(HttpServletResponse response, HttpServletRequest request,String user_code,String newPhone) throws Exception {
        String msg="";
        msg=shopService.updatePhone(user_code,newPhone);
        writeJson(msg,response);
    }

    @RequestMapping("delGridById")
    public void delGridById(HttpServletResponse response, HttpServletRequest request,String ids) throws Exception {
        Map<String,Object> map=new HashMap<>();
            shopService.delGridById(ids);
            map.put("success",true);
        writeJson(map,response);
    }


    /**
     * ajax 代理商
     */
    @RequestMapping("ajaxAgent")
    public void ajaxAgent(HttpServletResponse response) throws Exception {
        List<Agent> agent = shopService.queryAgentByCondition();
        writeJson(agent,response);
    }

    /**
     * ajax 品牌
     */
    @RequestMapping("ajaxBrand")
    public void ajaxBrand(String agent_code,String is90,HttpServletResponse response) throws Exception
    {
        List<Brand> brand = shopService.queryBrandByCondition(agent_code,is90);
        writeJson(brand,response);
    }

    /**
     * 查询所属仓库地址
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/loadStock")
    public void loadStock(HttpServletResponse response) throws Exception
    {
        JSONObject jSONObject = new JSONObject();
//        List<Map<String,Object>> list = new ArrayList<>();
        String code= ZkNode.getIstance().getJsonConfig().get("localCity").toString();
        String day= DateUtil.dateToSimpleString(new Date());
        String checkString=code+day;
        checkString= MD5.md5(checkString);
        String requestUrl = ConfigUtil.get("ERP_URL")+"/wharehouse/getWhareHouseByCityCode.action";
        jSONObject.put("code",code);
        jSONObject.put("check",checkString);
        String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
        x = URLDecoder.decode(x, "utf-8");
        if(x==null||x.trim().equals("")){
            // 失败
        }else {
//            list = JSON.parseArray(x, Map<String,Object>.class);
        }
        writeJson(x,response);
    }

    @RequestMapping("showBusiness")
    public void showBusiness (HttpServletResponse response, HttpServletRequest request)throws Exception{
        //必要的分页参数
        Map map = getParameterByRequest(request);

        Paging paging= shopService.showBusiness(map);

        writeJson(paging, response);
    }

    /**
     * 跳转行业页面
     * @param mv
     * @return
     */
    @RequestMapping("toBusiness")
    public ModelAndView toBusiness(ModelAndView mv){
        mv.setViewName("basic/business/business");
        return mv;
    }

    /**
     *保存行业信息
     * @param response
     * @param request
     * @param category
     * @throws Exception
     */
    @RequestMapping("doSaveBusiness")
    public void doSaveBusiness(HttpServletResponse response, HttpServletRequest request,Category category) throws Exception {
        String msg="";
        msg=shopService.doSaveBusiness(category);
        writeJson(msg,response);
    }

    /**
     * 根据id获取门店信息
     * @param response
     */
    @RequestMapping("toGetEdit")
    public void toGetEdit(HttpServletResponse response, HttpServletRequest request,String sid) throws Exception {
        Pshop pshop=shopService.toGetEdit(sid);
        writeJson(pshop,response);
    }


    /**
     * 获取门店列表
     * @param response
     */
    @RequestMapping("getShopListForSelect")
    public void getShopListForSelect(HttpServletResponse response, HttpServletRequest request,String pid) throws Exception {
        User user= (User) getShiroAttribute("user");
        List<Pshop> list=shopService.getShopListForSelect(pid,user);
        writeJson(list,response);
    }


    /**
     * 导出二维码
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportQrcode")
    public void exportQrcode(HttpServletResponse response,String id)
            throws Exception {
        Pshop pshop=shopService.toGetEdit(id);
        String url="";
        if(StringUtil.isNullOrEmpty(pshop.getQrcode()))
        {
            //永久参数为Shop_+店铺的id
            url=wxQrCodeService.getForeverStrQrCode("Shop_"+id);
            pshop.setQrcode(url);
            shopService.updateQrCode(pshop);
        }
        else
        {url=pshop.getQrcode();}
        if(StringUtil.isNullOrEmpty(url))
        {throw  new Exception("获取token失效");}
        download(url,pshop,response);
    }

//下载二维码
    private void download(String path,Pshop pshop, HttpServletResponse response) {
        try {
            URL url = new URL(path);
            URLConnection con = url.openConnection();
            // path是指欲下载的文件的路径。
           // File file = new File(path);
            // 取得文件名。
         //   String filename = file.getName();
            // response.setContentType("application/x-excel");
            String fileName=pshop.getSid()+".jpg";//生成文件名，由于u8格式不可以用中文，所以用id做文件名，直接转成jpg格式的图
            response.setContentType("application/binary;charset=utf-8");
            response.setHeader("Content-disposition", "attachment; filename="
                    + fileName);// 组装附件名称和格式
            int len = 0;
            // 以流的形式下载文件。
            InputStream is = con.getInputStream();
            //InputStream fis = new BufferedInputStream(new FileInputStream(path));
            OutputStream out = new BufferedOutputStream(
                    response.getOutputStream());
            byte[] buffer = new byte[5120];
            while ((len = is.read(buffer)) > 0) // 切忌这后面不能加 分号 ”;“
            {
                out.write(buffer, 0, len);// 向客户端输出，实际是把数据存放在response中，然后web服务器再去response中读取
            }
            is.close();
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    /**
     * 保存门店和商品的关联关系
     * @param response
     */
    @RequestMapping("doSaveGoodsAndShop")
    public void doSaveGoodsAndShop(HttpServletResponse response, HttpServletRequest request,String sid,String goodsIds) throws Exception {
        User user= (User) getShiroAttribute("user");
       String res=shopService.saveGoodsAndShop(sid,goodsIds,user);
        writeJson(res,response);
    }
    /**
     * 取消门店和商品的关联关系
     * @param response
     */
    @RequestMapping("doDropGoodsAndShop")
    public void doDropGoodsAndShop(HttpServletResponse response, HttpServletRequest request,String sid,String goodsIds) throws Exception {
        User user= (User) getShiroAttribute("user");
        String res=shopService.updateGoodsAndShopForDrop(sid,goodsIds,user);
        writeJson(res,response);
    }


    /**
     * 修改门店商品上下架
     * @param response
     */
    @RequestMapping("doEditGoodsAndShopUpAndDown")
    public void doEditGoodsAndShopUpAndDown(HttpServletResponse response, HttpServletRequest request,String sid,String goodsIds,String isSale) throws Exception {
        User user= (User) getShiroAttribute("user");
        String res=shopService.updateGoodsAndShopUpAndDown(sid,goodsIds,user,isSale);
        writeJson(res,response);
    }

    /**
     * 获取商品列表
     * @param response
     */
    @RequestMapping("showShopGoods")
    public void getGoods(HttpServletResponse response, HttpServletRequest request,String sid) throws Exception {
        //必要的分页参数
        Map map = getParameterByRequest(request);
        map.put("sid",sid);
        Paging paging= shopService.showShopGoods(map);
        writeJson(paging, response);
    }
}
