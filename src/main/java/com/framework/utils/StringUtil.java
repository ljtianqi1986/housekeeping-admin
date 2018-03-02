package com.framework.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 字符串相关方法
 *
 */
public class StringUtil
{

    /**
     * 格式化字符串
     *
     * 例：formateString("xxx{0}bbb",1) = xxx1bbb
     *
     * @param str
     * @param params
     * @return
     */
    public static String formateString(String str, String... params) {
        for (int i = 0; i < params.length; i++) {
            str = str.replace("{" + i + "}", params[i] == null ? "" : params[i]);
        }
        return str;
    }
    /**
     * 判断是否为空
     * @param obj
     * @return
     */
    public static boolean isNullOrEmpty(Object obj) {
        return obj == null || "".equals(obj.toString());
    }

    public static String toString(Object obj){
        if(obj == null) return "null";
        return obj.toString();
    }
    /**
     * 把字符串 和集合内容拼接
     * @param s
     * @param delimiter
     * @return
     */
    public static String join(Collection s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    /**
     * 将逗号分割的字符串 重新拼接 加入单引号  使得查询条件可用
     * @param arg
     * @return
     */
    public static String formatSqlIn(String arg){
        StringBuilder sb=new StringBuilder();
        if(!StringUtil.isNullOrEmpty(arg)){
            String[] temp=arg.split(",");
            for(int i=0;i<temp.length;i++){

                if(i!=(temp.length-1)){
                    sb.append("'").append(temp[i].trim()).append("'").append(",");
                }else{
                    sb.append("'").append(temp[i].trim()).append("'");
                }
            }

        }

        return sb.toString();
    }

	/**
	 * 将以逗号分隔的字符串转换成字符串数组
	 * 
	 * @param valStr
	 * @return String[]
	 */
	public static String[] StrList(String valStr)
	{
		int i = 0;
		String TempStr = valStr;
		String[] returnStr = new String[valStr.length() + 1
				- TempStr.replace(",", "").length()];
		valStr = valStr + ",";
		while (valStr.indexOf(',') > 0)
		{
			returnStr[i] = valStr.substring(0, valStr.indexOf(','));
			valStr = valStr.substring(valStr.indexOf(',') + 1, valStr.length());

			i++;
		}
		return returnStr;
	}
	
	public static boolean isNumeric(String str)
	{
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request){
        String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    public static String convertNullToEmpty(String str){
        if (str == null) {
            return "";
        }else{
            return str;
        }
    }
    /**
     * 将emoji表情替换成
     *
     * @param source
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source)
    {
        if (StringUtils.isNotBlank(source))
        {
            return source.replaceAll(
                    "[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
        } else
        {
            return source;
        }
    }

}
