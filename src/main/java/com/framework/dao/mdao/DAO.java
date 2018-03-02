package com.framework.dao.mdao;

import com.framework.model.Pager;
import com.framework.model.Paging;


public interface DAO<T> {
	
	/**
	 * 保存对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object save(String str, Object obj) throws Exception;
	
	/**
	 * 修改对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object update(String str, Object obj) throws Exception;
	
	/**
	 * 删除对象 
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object delete(String str, Object obj) throws Exception;

	/**
	 * 查找对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object findForObject(String str, Object obj) throws Exception;

	/**
	 * 查找对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object findForList(String str, Object obj) throws Exception;
	
	/**
	 * 查找对象封装成Map
	 * @param s
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object findForMap(String sql, Object obj, String key , String value) throws Exception;
	
	
	
	public Paging<T> findForPaging(String str, Object obj,String countStr,Object contObj)throws Exception;
	
	/**
	 * 查找分页数据
	 * @param str
	 * @param obj
	 * @param countStr
	 * @param contObj
	 * @return
	 * @throws Exception
	 */
	public Pager<T> findForPager(String str,String countStr,Pager<T> pager)throws Exception;
	
	
	
	/**
	 * 查找分页数据优化数据量查询
	 * @param str
	 * @param obj
	 * @param countStr
	 * @param contObj
	 * @return
	 * @throws Exception
	 */
	public Pager<T> findForPager1(String str,String countStr,Pager<T> pager)throws Exception;
}
