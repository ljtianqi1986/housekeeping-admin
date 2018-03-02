/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.framework.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PrintJSON {
	
	public static void write(HttpServletResponse response,String content) {
		response.reset();
		response.setContentType("application/json");
		response.setHeader("Cache-Control","no-store");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw=null;
		try{
			pw=response.getWriter();
			pw.write(content);
		}catch (IOException e){
			e.printStackTrace();
		}
		pw.flush();
	}

}
