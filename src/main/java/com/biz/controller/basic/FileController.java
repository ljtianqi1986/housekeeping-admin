package com.biz.controller.basic;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.biz.conf.Global;
import com.biz.model.Hmodel.basic.Tpics;
import com.biz.service.base.PicServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.FileUtil;
import com.framework.utils.OssUtils;
import com.framework.utils.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.biz.model.Hmodel.TFileType;
import com.biz.model.Pmodel.Pfile;
import com.biz.model.Pmodel.PfileLabel;
import com.biz.service.basic.FileServiceI;

/*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：UserController.java 描述说明 ：
 * 
 * 创建信息 : create by 刘佳佳 on 2016-6-14 下午3:11:14  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
@Controller
@RequestMapping("/file")
public class FileController extends BaseController{
	@Autowired
	private FileServiceI fileService;

	@Resource(name = "picService")
	private PicServiceI picService;

	private Image img;
	private int width;
	private int height;

	/**
	 * 跳转至文件管理页面
	 *
	 * @param mv
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toFileManager")
	public ModelAndView toHome(ModelAndView mv) throws Exception {
		//加载文件类型
		List<TFileType> filetypeList = fileService.find();
		//加载文件标签属性
		List<PfileLabel> fileLabelList = fileService.getFileLabel(null);
		//从Redis中加载文件信息
		String fileType = "";
		List<Pfile> fileList = fileService.getFileList(fileType);
		mv.addObject("fileTypeList", filetypeList);
		mv.addObject("fileLabelList", fileLabelList);
		mv.addObject("fileList", fileList);
		mv.setViewName("file/file_manager");
		return mv;
	}

	@RequestMapping("bulk")
	@ResponseBody
	public String bulk(HttpServletRequest request, MultipartFile file) {
		System.out.println(file.getName());
		return "1";
	}

	/**
	 * 上传图片
	 *
	 * @return
	 **/
	@RequestMapping(value = "/doUploadFile")
	public
	@ResponseBody
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
		width = img.getWidth(null); // 得到源图宽
		height = img.getHeight(null); // 得到源图长
		if (width / height > 400 / 400) {
			//	resizeByWidth(400,new_name);
		} else {
			//resizeByHeight(400,new_name);
		}
		// 读取本地资源
		response.setCharacterEncoding("utf-8");
		response.setContentType("image/jpg");
		//String serverImagePath = Const.UPLOAD_IMG_DIR_NEW + "/" + new_name;
		//File file = new File(serverImagePath);

		String key = UUID.randomUUID().toString();
		String newFileName = key;
//		String fileName = ConfigUtil.get("img");
		String fileName=Global.get("img");
		if (fileToUpload.getContentType().equals(Global.getJPEG())) {
			newFileName = fileName + FileUtil.getFileDir() + newFileName + ".jpg";
		} else if (fileToUpload.getContentType().equals(Global.getPNG())) {
			newFileName = fileName + FileUtil.getFileDir() + newFileName + ".png";
		}
		//读取缩略图上传阿里云
		//OssUtils.UploadSmallFile(file,newFileName);
		String etag = OssUtils.UploadFile(fileToUpload, newFileName);
		// 保存附件内容
		// 保存附件表
		Tpics pic = new Tpics();
		pic.setPath(newFileName);
		//	pic.setSize(file.getTotalSpace())
		String picId = (String) picService.save(pic);
		//String str = new_name + "$" + files.getId();
		Map<String, String> map = new HashMap<String, String>();
		map.put("path", newFileName);
		map.put("picId", picId);
		//删除
		//file.delete();
		writeJson(map, response);
	}


	public String getRndCode()
	{
		return UuidUtil.get32UUID();
	}
	
}
