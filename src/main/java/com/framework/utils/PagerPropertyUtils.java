package com.framework.utils;


import com.framework.model.Column;
import com.framework.model.Condition;
import com.framework.model.Pager;
import com.framework.model.Sort;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Pager属性映射工具类
 */
@SuppressWarnings("rawtypes")
public class PagerPropertyUtils {
	
	/**
	 * 将JSON对象映射为Pager对象
	 * @param JSONObject 原JSON对象
	 * @throws Exception
	 */
	public static Pager copy(JSONObject object) throws Exception{
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("parameters", Map.class);
		classMap.put("fastQueryParameters", Map.class);
		classMap.put("advanceQueryConditions", Condition.class);
		classMap.put("advanceQuerySorts", Sort.class);
		classMap.put("exhibitDatas", Map.class);
		classMap.put("exportColumns", Column.class);
		classMap.put("exportDatas", Map.class);
		Pager pager = (Pager)JSONObject.toBean(object, Pager.class, classMap);
		return pager;
	}
	
	
}