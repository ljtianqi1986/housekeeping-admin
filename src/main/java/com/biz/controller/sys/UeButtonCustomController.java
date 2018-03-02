package com.biz.controller.sys;

import com.biz.conf.Global;
import com.framework.controller.BaseController;
import com.framework.utils.OssUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ldd_person on 2017/2/17.
 */
@Controller
@RequestMapping("/ue")
public class UeButtonCustomController extends BaseController{

    @RequestMapping("/uploadImageForUE")
    public void uploadImageForUE(@RequestParam(value = "upfile", required = false) MultipartFile file,HttpServletResponse response) throws IOException {
        // UE需要的返因值
        String fileName = file.getOriginalFilename();
       String eTag = OssUtils.UploadFileForUe(file,Global.get("OSSKEYPREFIX")+fileName);
        // UE需要的返因值
        JSONObject jsobject = new JSONObject();
        if (eTag != null) {
            jsobject.put("state", "SUCCESS");
            jsobject.put("url", "/"+Global.get("OSSKEYPREFIX")+fileName);
            jsobject.put("title",fileName);
            jsobject.put("original", "1");
        } else {
            jsobject.put("state", "文件上传失败!");
            jsobject.put("url", "");
            jsobject.put("title", "");
            jsobject.put("original", "");
        }
       // response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(jsobject.toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
