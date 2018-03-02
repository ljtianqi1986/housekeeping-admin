package com.framework.utils;

import com.framework.model.Operator;
import com.framework.model.Params;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 刘佳佳 on 2016/7/25.
 * 用于通过指定的参数生产 特定的SQL 语句使用
 */
public class SqlFactory {


    /**
     * 查询数据sql
     * @param sql
     * @param params
     * @param sqlparams
     * @return
     */
    public String getSql(String sql,Map<String, Object> params,Params sqlparams){
        if (params!=null&&!params.isEmpty()){
            Iterator<Map.Entry<String, Object>> entries = params.entrySet().iterator();
            sql+=" where 1=1";
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                String key=entry.getKey().trim();
                String value=entry.getValue().toString().trim();
                //分离规则
                String[] keys=key.split("_");
                String rule=keys[0].toString().trim();
                String column=keys[1].toString().trim();
                String tag="";
                try{
                    tag =keys[2];
                }catch (Exception e){}
                String column2=column+tag;

                //模糊查询
                if (rule.equals(Operator._LIKE)&&!StringUtil.isNullOrEmpty(value)){
                    sql+=StringUtil.formateString(" and {0} like :{2} ", column, value,column2);
                }
                //精准查询
                if (rule.equals(Operator._EQUALS)&&!StringUtil.isNullOrEmpty(value)){
                    sql+=StringUtil.formateString(" and {0} = :{2} ", column, value,column2);
                }
                //大于
                if (rule.equals(Operator._GT)&&!StringUtil.isNullOrEmpty(value)){
                    sql+=StringUtil.formateString(" and {0} > :{2} ", column, value,column2);
                }
                if (rule.equals(Operator._LT)&&!StringUtil.isNullOrEmpty(value)){
                    sql+=StringUtil.formateString(" and {0} < :{2} ", column, value,column2);
                }

            }
            if (sqlparams!=null&&!StringUtil.isNullOrEmpty(sqlparams.getSort())){
                sql+=StringUtil.formateString(" order by {0} {1}",sqlparams.getSort(),sqlparams.getOrder());
            }
        }
        return sql;
    }

    /**
     * 查询数据量sql
     * @param sql
     * @param params
     * @return
     */
    public String getCountSql(String sql,Map<String, Object> params){
        if (params!=null&&!params.isEmpty()){
            Iterator<Map.Entry<String, Object>> entries = params.entrySet().iterator();
            sql+=" where 1=1";
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                String key=entry.getKey().trim();
                String value=entry.getValue().toString().trim();
                //分离规则
                String[] keys=key.split("_");
                String rule=keys[0].toString().trim();
                String column=keys[1].toString().trim();
                String tag="";
                try{
                    tag =keys[2];
                }catch (Exception e){}
                String column2=column+tag;
                //模糊查询
                if (rule.equals(Operator._LIKE)&&!StringUtil.isNullOrEmpty(value)){
                    sql+=StringUtil.formateString(" and {0} like :{2} ", column, value,column2);
                }
                //精准查询
                if (rule.equals(Operator._EQUALS)&&!StringUtil.isNullOrEmpty(value)){
                    sql+=StringUtil.formateString(" and {0} = :{2} ", column, value,column2);
                }
                //大于
                if (rule.equals(Operator._GT)&&!StringUtil.isNullOrEmpty(value)){
                    sql+=StringUtil.formateString(" and {0} > :{2} ", column, value,column2);
                }
                if (rule.equals(Operator._LT)&&!StringUtil.isNullOrEmpty(value)){
                    sql+=StringUtil.formateString(" and {0} < :{2} ", column, value,column2);
                }
            }
        }
        return sql;
    }


    /**
     * 获取 Params实例
     * @return
     */
    public Params getParams(){
        return new Params();
    }

    /**
     * 获取去除掉规则的Params
     * @param params
     * @return
     */
    public Map<String, Object> getColumnParams(Map<String, Object> params){
        Map<String, Object> paramsMap=new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> entries =params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            String key = entry.getKey().trim();
            String value = entry.getValue().toString().trim();
            //分离规则
            String[] keys = key.split("_");
            String tag="";
            try{
                tag =keys[2];
            }catch (Exception e){}
            if(!StringUtil.isNullOrEmpty(value)){
                paramsMap.put(keys[1].toString().trim()+tag,value);
            }

    }
        return paramsMap;
    }

}
