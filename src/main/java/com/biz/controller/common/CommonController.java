package com.biz.controller.common;

import com.biz.model.Hmodel.basic.Tpics;
import com.biz.service.base.PicServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 公共资源调用区
 * Created by liujiajia on 2017/1/17.
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {
    @Resource(name = "picService")
    private PicServiceI picService;
    private Image img;
    /**
     * 获取验证码图片和文本(验证码文本会保存在HttpSession中)
     */
    @RequestMapping("/genCaptcha")
    public void genCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置页面不缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String verifyCode = VerifyCodeUtil.generateVerifyCode(4);
        //将验证码放到HttpSession里面
        request.getSession().setAttribute("code", verifyCode);
        System.out.println("本次生成的验证码为[" + verifyCode + "],已存放到HttpSession中");
        //设置输出的内容的类型为JPEG图像
        response.setContentType("image/jpeg");
        //生成图片
        int w = 100, h = 30;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);

    }

    /**
     * 上传文件
     *
     * @return
     * **/
    @RequestMapping(value = "/doUploadFile")
    public @ResponseBody
    void doUploadFile(MultipartFile fileToUpload, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rnd_name = getRndCode();
        // 扩展名格式：

        String ext_name = "";
        if (fileToUpload.getOriginalFilename().lastIndexOf(".") >= 0) {
            ext_name = fileToUpload.getOriginalFilename().substring(
                    fileToUpload.getOriginalFilename().lastIndexOf("."));
        }
        String new_name = rnd_name + ext_name;
        ByteArrayInputStream bais = new ByteArrayInputStream(fileToUpload.getBytes());
        MemoryCacheImageInputStream mciis = new MemoryCacheImageInputStream(bais);
        img = ImageIO.read(mciis); // 构造Image对象
 /*       width = img.getWidth(null); // 得到源图宽
        height = img.getHeight(null); // 得到源图长
        if (width / height > 400 / 400) {
            //	resizeByWidth(400,new_name);
        } else {
            //resizeByHeight(400,new_name);
        }*/
        // 读取本地资源
        response.setCharacterEncoding("utf-8");
        response.setContentType("image/jpg");
        //String serverImagePath = Const.UPLOAD_IMG_DIR_NEW + "/" + new_name;
        //File file = new File(serverImagePath);

        String key= UUID.randomUUID().toString();
        String newFileName=key;
        String fileName= ConfigUtil.get("img");
        if(fileToUpload.getContentType().equals("image/jpeg")){
            newFileName=fileName+ FileUtil.getFileDir()+newFileName+".jpg";
        }else if(fileToUpload.getContentType().equals("image/png")){
            newFileName=fileName+ FileUtil.getFileDir()+newFileName+".png";
        }
        //读取缩略图上传阿里云
        //OssUtils.UploadSmallFile(file,newFileName);
        String etag= OssUtils.UploadFile(fileToUpload,newFileName);
        // 保存附件内容
        // 保存附件表
        Tpics pic = new Tpics();
        pic.setPath(newFileName);
        //	pic.setSize(file.getTotalSpace())
        String picId=(String) picService.save(pic);
        //String str = new_name + "$" + files.getId();
        Map<String, String> map=new HashMap<String, String>();
        map.put("path", newFileName);
        map.put("picId", picId);
        //删除
        //file.delete();
        writeJson(map, response);
    }



}
