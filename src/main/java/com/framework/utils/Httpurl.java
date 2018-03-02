package com.framework.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * 
 */
public class Httpurl {


    /**
     * 远程连接HttpURL
     * @param url
     * @return
     */
    public static String  HttpURL_link(String url){
        String result = "";
        try {
            URL localURL = new URL(url);
            String async = "";
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);

            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader reader = null;
            StringBuffer resultBuffer = new StringBuffer();
            String tempLine = null;

            if (httpURLConnection.getResponseCode() >= 300)
            {
                throw new Exception(
                        "HTTP Request is not success, Response code is "
                                + httpURLConnection.getResponseCode());
            }
            try
            {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                reader = new BufferedReader(inputStreamReader);
                String asynckl = "";
                while ((tempLine = reader.readLine()) != null)
                {
                    resultBuffer.append(tempLine);
                }

            } finally
            {

                if (reader != null)
                {
                    reader.close();
                }

                if (inputStreamReader != null)
                {
                    inputStreamReader.close();
                }

                if (inputStream != null)
                {
                    inputStream.close();
                }

            }
            result = resultBuffer.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("=远程连接HttpURL报错==" ,e.fillInStackTrace());
            return result;
        }
    }

    public static String getKeyByParam(String dxyCode, String dxyPersonCode, String orderCode)
    {
        String key = "";
        try {
            StringBuffer key_sb = new StringBuffer();
            key_sb.append("SHOPCODE=");
            key_sb.append(dxyCode);
            key_sb.append("&USERCODE=");
            key_sb.append(dxyPersonCode);
            key_sb.append("&ORDERCODE=");
            key_sb.append(orderCode);
            System.err.println(key_sb);
            key = CryptTool.md5Digest(key_sb.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return key;
    }

    /**
     * 远程连接HttpURL
     * @param url
     * @return
     */
    public static String HttpURL(String url){
        String result = "";
        try {
            URL localURL = new URL(url);
            String async = "";
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);

            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader reader = null;
            StringBuffer resultBuffer = new StringBuffer();
            String tempLine = null;

            if (httpURLConnection.getResponseCode() >= 300)
            {
                throw new Exception(
                        "HTTP Request is not success, Response code is "
                                + httpURLConnection.getResponseCode());
            }
            try
            {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                reader = new BufferedReader(inputStreamReader);
                String asynckl = "";
                while ((tempLine = reader.readLine()) != null)
                {
                    resultBuffer.append(tempLine);
                }

            } finally
            {

                if (reader != null)
                {
                    reader.close();
                }

                if (inputStreamReader != null)
                {
                    inputStreamReader.close();
                }

                if (inputStream != null)
                {
                    inputStream.close();
                }

            }
            result = resultBuffer.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("=远程连接HttpURL报错==" ,e.fillInStackTrace());
            return result;
        }
    }
}
