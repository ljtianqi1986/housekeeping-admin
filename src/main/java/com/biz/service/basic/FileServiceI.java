package com.biz.service.basic;

import java.util.List;

import com.biz.model.Hmodel.TFileType;
import com.biz.model.Pmodel.Pfile;
import com.biz.model.Pmodel.PfileLabel;
import com.biz.service.base.BaseServiceI;


/**
 * Created by yzljj on 2016/6/23.
 */
public interface FileServiceI extends BaseServiceI<TFileType> {

	/**
	 * 通过文件类型加载文件标签（文件类型可为空）
	 * @param fileType
	 * @return List<TFileLabel>
	 */
	List<PfileLabel> getFileLabel(String fileType) throws Exception;

	/**
	 * 通过文件类型加载文件列表
	 * @param fileType
	 */
	List<Pfile> getFileList(String fileType)throws Exception;
}
