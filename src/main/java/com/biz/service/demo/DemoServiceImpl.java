package com.biz.service.demo;

import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.SqlFactory;
import com.framework.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by yzljj on 2016/6/23.
 */
@Service("demoService")
public class DemoServiceImpl extends BaseServiceImpl<Object> implements DemoServiceI {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

    @Autowired
    private BaseDaoI baseDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map> showJqGrid() throws Exception {
        List<Map> list= (List<Map>)dao.findForList("demoDao.showGrid", null);
		return list;
	}

    @Override
    public Paging getDemoPage(Params sqlParams, Map<String, Object> params) throws Exception {
        String sql="select * from demo_grid ";
        String countSql="select count(1) from demo_grid ";
        SqlFactory factory=new SqlFactory();
        System.out.println("AA;"+factory.getSql(sql,params,sqlParams));
        System.out.println("BB;"+factory.getCountSql(countSql, params));
        Paging paging=baseDao.page(factory.getSql(sql,params,sqlParams),
                factory.getCountSql(countSql, params), sqlParams,
                factory.getColumnParams(params));
        return paging;
    }

    @Override
    public void delGridById(String ids) throws Exception {
        String sql= StringUtil.formateString("update demo_grid set state=1 where id in({0})",StringUtil.formatSqlIn(ids));
        baseDao.executeSql(sql);
    }


}
