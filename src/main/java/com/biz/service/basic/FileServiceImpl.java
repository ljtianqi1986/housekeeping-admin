package com.biz.service.basic;

import com.biz.model.Hmodel.TFileLabel;
import com.biz.model.Hmodel.TFileType;
import com.biz.model.Pmodel.Pfile;
import com.biz.model.Pmodel.PfileLabel;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.DateUtil;
import com.framework.utils.ListTranscoder;
import com.framework.utils.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by yzljj on 2016/6/23.
 */
@Service("fileService")
public class FileServiceImpl extends BaseServiceImpl<TFileType> implements  FileServiceI {

	@SuppressWarnings("unused")
	@Autowired
	private BaseDaoI<TFileLabel> fileLabelDao;
	
	@Autowired
	private DaoSupport<TFileLabel> dao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PfileLabel> getFileLabel(String fileType) throws Exception {
		List<PfileLabel> pfileLabelList=(List<PfileLabel>)dao.findForList("fileDao.findFileLabel", fileType);
		return pfileLabelList;
	}

	@Override
	public List<Pfile> getFileList(String fileType) throws Exception {
		byte[] key=SerializeUtil.serialize("r_filelist5");
		List<Pfile> pfileList=new ArrayList<Pfile>();
		ListTranscoder<Pfile> lt=new ListTranscoder<Pfile>();
		Pfile file=new Pfile();
		file.setId(UUID.randomUUID().toString());
		file.setFileName("最美的风景12.jpg");
		file.setFilePath("http://static16.photo.sina.com.cn/middle/6c855bb5t92dd31ee43df&690");
		file.setFileSize("1409kb");
		file.setCreateTime(DateUtil.dateToString(new Date()));


//		Pfile file2=new Pfile();
//		file2.setId(UUID.randomUUID().toString());
//		file2.setFileName("最美的风景13.jpg");
//		file2.setFilePath("http://img15.3lian.com/2015/a1/10/d/111.jpg");
//		file2.setFileSize("1109kb");
//		file2.setCreateTime(DateUtil.dateToString(new Date()));
//
//		redisDao.rpush(key, SerializeUtil.serialize(file));
//		redisDao.rpush(key, SerializeUtil.serialize(file2));
		//redisDao.set(key, lt.serialize(filelist));
		//filelist.add(file);
		//redisDao.append(key, SerializeUtil.serialize(filelist));
		//redisDao.sadd(key, SerializeUtil.serialize(file));
		//redisDao.plull(key, SerializeUtil.serialize(file));
		//List _fileList=(List)SerializeUtil.unserialize((redisDao.get(key)));
		//redisDao.rpush(key, lt.serialize(filelist));
		//Object oo=SerializeUtil.unserialize(redisDao.get(key));
		
		//Object _fileList=lt.deserialize(redisDao.get(key));
//		List<byte[]> bytelist=redisDao.lrange(key, 0, -1);
//		for (byte[] bs : bytelist) {
//			Pfile lf=(Pfile)SerializeUtil.unserialize(bs);
//			pfileList.add(lf);
//			System.out.println(lf.getFileName());
//		}
		return pfileList;
	}
   
}
