package com.framework.dao.mdao;

import com.framework.model.Pager;
import com.framework.model.Paging;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;



@Repository("daoSupport")
public class DaoSupport<T> implements DAO<T> {

	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;
	
	/**
	 * 保存对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object save(String str, Object obj) throws Exception {
		return sqlSessionTemplate.insert(str, obj);
	}
	
	/**
	 * 批量更新
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public Object batchSave(String str, List objs )throws Exception{
		return sqlSessionTemplate.insert(str, objs);
	}
	
	/**
	 * 修改对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object update(String str, Object obj) throws Exception {
		return sqlSessionTemplate.update(str, obj);
	}

	/**
	 * 批量更新
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public void batchUpdate(String str, List objs )throws Exception{
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		//批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
		try{
			if(objs!=null){
				for(int i=0,size=objs.size();i<size;i++){
					sqlSession.update(str, objs.get(i));
				}
				sqlSession.flushStatements();
				sqlSession.commit();
				sqlSession.clearCache();
			}
		}finally{
			sqlSession.close();
		}
	}
	
	/**
	 * 批量更新
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public Object batchDelete(String str, List objs )throws Exception{
		return sqlSessionTemplate.delete(str, objs);
	}
	
	/**
	 * 删除对象 
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object delete(String str, Object obj) throws Exception {
		return sqlSessionTemplate.delete(str, obj);
	}
	 
	/**
	 * 查找对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object findForObject(String str, Object obj) throws Exception {
		return sqlSessionTemplate.selectOne(str, obj);
	}

	/**
	 * 查找对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object findForList(String str, Object obj) throws Exception {
		return sqlSessionTemplate.selectList(str, obj);
	}
	
	public Object findForMap(String str, Object obj, String key, String value) throws Exception {
		return sqlSessionTemplate.selectMap(str, obj, key);
	}

	
	/**
	 * 查找对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@Override
	public Paging findForPaging(String str, Object obj,String countStr,Object contObj) throws Exception {
		List<T> list=sqlSessionTemplate.selectList(str, obj);
		List<T> listcount=sqlSessionTemplate.selectList(countStr, contObj);
		Paging paging=new Paging();
		if(listcount!=null&&listcount.size()>0){
			paging.setTotal(Long.parseLong(String.valueOf(listcount.size())));
		}else{
			paging.setTotal(0l);
		}
		paging.setRows(list);
		return paging;
	}
	public Paging<Map<String, Object>> findForPagings(String str, Object obj,String countStr,Object contObj) {
		List<T> list=sqlSessionTemplate.selectList(str, obj);
		int count=sqlSessionTemplate.selectOne(countStr, contObj);
		Paging paging=new Paging();
		if(count!=0){
			paging.setTotal(Long.parseLong(String.valueOf(count)));
		}else{
			paging.setTotal(0l);
		}
		paging.setRows(list);
		return paging;
	}

	@Override
	public Pager<T> findForPager(String str, String countStr,
			Pager<T> pager) throws Exception {
		
		try {
			int pageSize = pager.getPageSize();
			int startRecord = pager.getStartRecord();
			int recordCount = pager.getRecordCount();
			int pageCount = pager.getPageCount();
		
			Map<String, Object> parameters = pager.getParameters();
			parameters.put("begin", startRecord);
			parameters.put("rows", pageSize);
			
			List<T> list = sqlSessionTemplate.selectList(str, parameters);
			List<T> listcount = sqlSessionTemplate.selectList(countStr,parameters);
			
			recordCount = listcount.size();
			
			pager.setRecordCount(recordCount);
			pageCount = recordCount / pageSize
					+ (recordCount % pageSize > 0 ? 1 : 0);
			pager.setPageCount(pageCount);
			pager.setExhibitDatas(list);
			pager.setIsSuccess(true);
			
		} catch (Exception e) {
			pager.setIsSuccess(false);
		}
		return pager;
	}
	
	
	
	@Override
	public Pager<T> findForPager1(String str, String countStr,
			Pager<T> pager) throws Exception {
//		Date date1 = new Date();
		try {
			int pageSize = pager.getPageSize();
			int startRecord = pager.getStartRecord();
			int recordCount = pager.getRecordCount();
			int pageCount = pager.getPageCount();
		
			Map<String, Object> parameters = pager.getParameters();
			parameters.put("begin", startRecord);
			parameters.put("rows", pageSize);
//			Date date2 = new Date();
			List<T> list = sqlSessionTemplate.selectList(str, parameters);
//			Date date3 = new Date();
			Integer count1 = sqlSessionTemplate.selectOne(countStr,parameters);
			recordCount = count1.intValue();
//			Date date4 = new Date();
			pager.setRecordCount(recordCount);
			pageCount = recordCount / pageSize
					+ (recordCount % pageSize > 0 ? 1 : 0);
			pager.setPageCount(pageCount);
			pager.setExhibitDatas(list);
			pager.setIsSuccess(true);
			
//			System.out.println("数据加载时间"+(date3.getTime()-date2.getTime()));
//			System.out.println("count加载时间"+(date4.getTime()-date3.getTime()));
			
			
		} catch (Exception e) {
			e.printStackTrace();
			pager.setIsSuccess(false);
		}
//		Date date5 = new Date();
//		
//		System.out.println("dao返回时间"+(date5.getTime()-date1.getTime()));
		return pager;
	}
	
}


