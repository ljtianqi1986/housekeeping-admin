package com.biz.service.base;

import com.framework.dao.hdao.BaseDaoI;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.HqlFilter;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.*;


/**
 * 基础业务逻辑
 * 
 * @author 刘佳佳
 * 
 * @param <T>
 */
//@Service("baseService")
public class BaseServiceImpl<T> implements BaseServiceI<T> {

	@Autowired
	private BaseDaoI<T> baseDao;

	@Autowired
	private ServletContext sc;


	@Override
	public Serializable save(T o) {
		return baseDao.save(o);
	}

	@Override
	public void delete(T o) {
		baseDao.delete(o);
	}

	@Override
	public void update(T o) {
		baseDao.update(o);
	}

	@Override
	public void saveOrUpdate(T o) {
		baseDao.saveOrUpdate(o);
	}

	@Override
	public T getById(Serializable id) {
		Class<T> c = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return baseDao.getById(c, id);
	}

	@Override
	public T getByHql(String hql) {
		return baseDao.getByHql(hql);
	}

	@Override
	public T getByHql(String hql, Map<String, Object> params) {
		return baseDao.getByHql(hql, params);
	}

	@Override
	public T getByFilter(HqlFilter hqlFilter) {
		String className = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
		String hql = "select distinct t from " + className + " t";
		return getByHql(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
	}

	@Override
	public List<T> find() {
		return findByFilter(new HqlFilter());
	}

	@Override
	public List<T> find(String hql) {
		return baseDao.find(hql);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		return baseDao.find(hql, params);
	}

	@Override
	public List<T> findByFilter(HqlFilter hqlFilter) {
		String className = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
		String hql = "select distinct t from " + className + " t";
		return find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
	}

	@Override
	public List<T> find(String hql, int page, int rows) {
		return baseDao.find(hql, page, rows);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		return baseDao.find(hql, params, page, rows);
	}

	@Override
	public List<T> find(int page, int rows) {
		return findByFilter(new HqlFilter(), page, rows);
	}

	@Override
	public List<T> findByFilter(HqlFilter hqlFilter, int page, int rows) {
		String className = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
		String hql = "select distinct t from " + className + " t";
		return find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams(), page, rows);
	}

	@Override
	public Long count(String hql) {
		return baseDao.count(hql);
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		return baseDao.count(hql, params);
	}

	@Override
	public Long countByFilter(HqlFilter hqlFilter) {
		String className = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
		String hql = "select count(distinct t) from " + className + " t";
		return count(hql + hqlFilter.getWhereHql(), hqlFilter.getParams());
	}

	@Override
	public Long count() {
		return countByFilter(new HqlFilter());
	}

	@Override
	public int executeHql(String hql) {
		return baseDao.executeHql(hql);
	}

	@Override
	public int executeHql(String hql, Map<String, Object> params) {
		return baseDao.executeHql(hql, params);
	}

	@Override
	public List findBySql(String sql) {
		return baseDao.findBySql(sql);
	}

	@Override
	public List findBySql(String sql, int page, int rows) {
		return baseDao.findBySql(sql, page, rows);
	}

	@Override
	public List findBySql(String sql, Map<String, Object> params) {
		return baseDao.findBySql(sql, params);
	}

	@Override
	public List findBySql(String sql, Map<String, Object> params, int page, int rows) {
		return baseDao.findBySql(sql, params, page, rows);
	}

	@Override
	public int executeSql(String sql) {
		return baseDao.executeSql(sql);
	}

	@Override
	public int executeSql(String sql, Map<String, Object> params) {
		return baseDao.executeSql(sql, params);
	}

	@Override
	public BigInteger countBySql(String sql) {
		return baseDao.countBySql(sql);
	}

	@Override
	public BigInteger countBySql(String sql, Map<String, Object> params) {
		return baseDao.countBySql(sql, params);
	}

	@Override
	public Paging page(HqlFilter hqlFilter, int page, int rows) {
		Paging paging=new Paging();
		List<T> list=findByFilter(hqlFilter, page, rows);
		long count=countByFilter(hqlFilter);
		paging.setTotal(count);
		paging.setRows(list);
		return paging;
	}

    @Override
    public Paging getPage(String sql,String countSql,int page, int rows) {
        Paging paging=new Paging();
        Map<String, Object> params=new HashMap<String, Object>();
        page=page/rows+1;
        List<Map> resultList=baseDao.findBySql(sql,params,page,rows);
        BigInteger count=baseDao.countBySql(countSql, params);
        paging.setTotal(count.longValue());
        paging.setRows(resultList);
        return paging;
    }

	@Override
	public String getCurrentVersion() {
		Model model=null;
		String path = sc.getRealPath(File.separator)+"META-INF"+File.separator+"maven"+File.separator+"com.xmarket"+File.separator+"xmarket"+File.separator+"pom.xml";
		MavenXpp3Reader reader = new MavenXpp3Reader();
		try {
				model = reader.read(new FileReader(path));
		} catch (Exception e) {
			//ignore
		}
		///Users/liujiajia/Desktop/xmarket/target/xmarket-0.0.3-SNAPSHOT/META-INF/maven/com.xmarket/xmarket/pom.xml
		return model==null?"开发版":model.getVersion();
	}


	@Override
	public Pager<Map<String, Object>> calculateAndTransformByMap(Map map)throws Exception{
		Pager<Map<String, Object>> pager = new Pager();

		int limit = Integer.parseInt(map.get("limit").toString());
		int offset = Integer.parseInt(map.get("offset").toString());
		pager.setParameters(map);
		pager.setStartRecord(offset);
		pager.setPageSize(limit);

		return pager;
	}



	public long getRndNumCode()
	{
		Random rand = new Random();
		Date now = new Date();
		long t = now.getTime();
		return Long.valueOf(t * 1000L + (long)rand.nextInt(1000));
	}

}
