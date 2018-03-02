package com.biz.service.base;


import com.biz.model.Pmodel.User;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.HqlFilter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


/**
 * 基础业务逻辑类，其他service继承此service获得基本的业务
 * 
 * @author 刘佳佳
 * 
 * @param <T>
 */
public interface BaseServiceI<T> {

	/**
	 * 保存一个对象
	 * 
	 * @param o
	 *            对象
	 * @return 对象的ID
	 */
	public Serializable save(T o);

	/**
	 * 删除一个对象
	 * 
	 * @param o
	 *            对象
	 */
	public void delete(T o);

	/**
	 * 更新一个对象
	 * 
	 * @param o
	 *            对象
	 */
	public void update(T o);

	/**
	 * 保存或更新一个对象
	 * 
	 * @param o
	 *            对象
	 */
	public void saveOrUpdate(T o);

	/**
	 * 通过主键获得对象
	 * 
	 * 类名.class
	 * @param id
	 *            主键
	 * @return 对象
	 */
	public T getById(Serializable id);

	/**
	 * 通过HQL语句获取一个对象
	 * 
	 * @param hql
	 *            HQL语句
	 * @return 对象
	 */
	public T getByHql(String hql);

	/**
	 * 通过HQL语句获取一个对象
	 * 
	 * @param hql
	 *            HQL语句
	 * @param params
	 *            参数
	 * @return 对象
	 */
	public T getByHql(String hql, Map<String, Object> params);

	/**
	 * 通过HqlFilter获取一个对象
	 * 
	 * @param hqlFilter
	 * @return
	 */
	public T getByFilter(HqlFilter hqlFilter);

	/**
	 * 获得对象列表
	 * 
	 * @return
	 */
	public List<T> find();

	/**
	 * 获得对象列表
	 * 
	 * @param hql
	 *            HQL语句
	 * @return List
	 */
	public List<T> find(String hql);

	/**
	 * 获得对象列表
	 * 
	 * @param hql
	 *            HQL语句
	 * @param params
	 *            参数
	 * @return List
	 */
	public List<T> find(String hql, Map<String, Object> params);

	/**
	 * 获得对象列表
	 * 
	 * @param hqlFilter
	 * @return
	 */
	public List<T> findByFilter(HqlFilter hqlFilter);

	/**
	 * 获得分页后的对象列表
	 * 
	 * @param hql
	 *            HQL语句
	 * @param page
	 *            要显示第几页
	 * @param rows
	 *            每页显示多少条
	 * @return List
	 */
	public List<T> find(String hql, int page, int rows);

	/**
	 * 获得分页后的对象列表
	 * 
	 * @param hql
	 *            HQL语句
	 * @param params
	 *            参数
	 * @param page
	 *            要显示第几页
	 * @param rows
	 *            每页显示多少条
	 * @return List
	 */
	public List<T> find(String hql, Map<String, Object> params, int page, int rows);

	/**
	 * 获得分页后的对象列表
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<T> find(int page, int rows);

	/**
	 * 获得分页后的对象列表
	 * 
	 * @param hqlFilter
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<T> findByFilter(HqlFilter hqlFilter, int page, int rows);

	/**
	 * 统计数目
	 * 
	 * @param hql
	 *            HQL语句(select count(*) from T)
	 * @return long
	 */
	public Long count(String hql);

	/**
	 * 统计数目
	 * 
	 * @param hql
	 *            HQL语句(select count(*) from T where xx = :xx)
	 * @param params
	 *            参数
	 * @return long
	 */
	public Long count(String hql, Map<String, Object> params);

	/**
	 * 统计数目
	 * 
	 * @param hqlFilter
	 * @return
	 */
	public Long countByFilter(HqlFilter hqlFilter);

	/**
	 * 统计数目
	 * 
	 * @return long
	 */
	public Long count();

	/**
	 * 执行一条HQL语句
	 * 
	 * @param hql
	 *            HQL语句
	 * @return 响应结果数目
	 */
	public int executeHql(String hql);

	/**
	 * 执行一条HQL语句
	 * 
	 * @param hql
	 *            HQL语句
	 * @param params
	 *            参数
	 * @return 响应结果数目
	 */
	public int executeHql(String hql, Map<String, Object> params);

	/**
	 * 获得结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 结果集
	 */
	public List findBySql(String sql);

	/**
	 * 获得结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @param page
	 *            要显示第几页
	 * @param rows
	 *            每页显示多少条
	 * @return 结果集
	 */
	public List findBySql(String sql, int page, int rows);

	/**
	 * 获得结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数
	 * @return 结果集
	 */
	public List findBySql(String sql, Map<String, Object> params);

	/**
	 * 获得结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数
	 * @param page
	 *            要显示第几页
	 * @param rows
	 *            每页显示多少条
	 * @return 结果集
	 */
	public List findBySql(String sql, Map<String, Object> params, int page, int rows);

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 响应行数
	 */
	public int executeSql(String sql);

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数
	 * @return 响应行数
	 */
	public int executeSql(String sql, Map<String, Object> params);

	/**
	 * 统计
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 数目
	 */
	public BigInteger countBySql(String sql);

	/**
	 * 统计
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数
	 * @return 数目
	 */
	public BigInteger countBySql(String sql, Map<String, Object> params);

	/**
	 * 分页
	 * @param hqlFilter
	 * @param page
	 * @param rows
	 * @return
	 */
	public Paging page(HqlFilter hqlFilter, int page, int rows);

    /**
     * 建议不要再Service中直接调用分页方法 可以重新建立业务相关的分页方法
     * @param sql           sql查询
     * @param countSql      sql数据量查询
     * @param page          当前页码
     * @param rows          每页数据量
     * @return
     */
    public Paging getPage(String sql,String countSql,int page, int rows);


	/**
	 *
	 * @return
	 */
	public String getCurrentVersion();


	/**
	 * 统计并且转换为适合的格式
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Pager<Map<String, Object>> calculateAndTransformByMap(Map map)throws Exception;


}
