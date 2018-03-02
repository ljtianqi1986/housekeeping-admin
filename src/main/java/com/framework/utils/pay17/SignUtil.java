package com.framework.utils.pay17;

import com.alibaba.fastjson.JSONObject;
import com.framework.utils.CryptTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ldd_person on 2017/3/1.
 */
public class SignUtil {

    public static String getSign(JSONObject jsonObject, String dxy_key){
        ArrayList<String> list = new ArrayList<String>();
        for(String key:jsonObject.keySet()){
            if(StringUtils.isNotBlank(jsonObject.get(key).toString())){
                list.add(key + "=" + jsonObject.get(key).toString() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + dxy_key;
        try {
            result = CryptTool.md5Digest(result).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
