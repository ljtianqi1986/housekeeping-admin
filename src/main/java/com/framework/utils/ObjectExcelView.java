package com.framework.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
/**
* 导入到EXCEL
* 类名称：ObjectExcelView.java
* 类描述： 
* @author FH
* 作者单位： 
* 联系方式：
* @version 1.0
 */
public class ObjectExcelView extends AbstractExcelView{

	public String newFileName="";
	
	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Date date = new Date();
		String filename = this.date2Str(date, "yyyyMMddHHmmss");
		if(newFileName!=null && !newFileName.trim().equals("")){
			filename=newFileName;
		}
		HSSFSheet sheet;
		HSSFCell cell;
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes( "gb2312" ), "ISO8859-1" )+".xlsx");
		sheet = workbook.createSheet("sheet1");

		HSSFCellStyle headerStyle = workbook.createCellStyle(); //标题样式
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont headerFont = workbook.createFont();	//标题字体
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short)11);
		headerStyle.setFont(headerFont);

		HSSFCellStyle contentStyle = workbook.createCellStyle(); //内容样式
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		/*
		 *  创建总计
		 */
		HashMap<String,List<String>> total = (HashMap<String,List<String>>) model.get("total");
		int start_header = 0;
		if(total != null){

			List<String> total_header = total.get("total_header");
			int len = total_header.size();
			for(int i=0;i<len;i++){
				String title = total_header.get(i);
				cell = getCell(sheet,0,i+1);
				cell.setCellStyle(headerStyle);
				setText(cell,title);
			}
			List<String> total_content = total.get("total_content");
			int content_len = total_content.size();

			for(int i=0;i<content_len; i++){
				String content = total_content.get(i);
				int row = i/(len+1)+1;
				int col = i%(len+1);
				cell = getCell(sheet, row, col);
				cell.setCellStyle(contentStyle);
				setText(cell,content);
			}
			start_header = content_len/(len+1)+2;
		}

		List<String> titles = (List<String>) model.get("titles");
		int len = titles.size();
//		HSSFCellStyle headerStyle = workbook.createCellStyle(); //标题样式
//		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		HSSFFont headerFont = workbook.createFont();	//标题字体
//		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//		headerFont.setFontHeightInPoints((short)11);
//		headerStyle.setFont(headerFont);
		short width = 20,height=25*20;
		sheet.setDefaultColumnWidth(width);
		for(int i=0; i<len; i++){ //设置标题
			String title = titles.get(i);
			cell = getCell(sheet, 0+start_header, i);
			cell.setCellStyle(headerStyle);
			setText(cell,title);
		}
		sheet.getRow(0).setHeight(height);

//		HSSFCellStyle contentStyle = workbook.createCellStyle(); //内容样式
//		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		List<Map<String,Object>> varList = (List<Map<String,Object>>) model.get("varList");
		int varCount = varList.size();
		for(int i=0; i<varCount; i++){
            Map<String,Object> vpd = varList.get(i);
			for(int j=0;j<len;j++){
				String varstr = StringUtils.isNotBlank(String.valueOf(vpd.get("var"+(j+1)))) ?String.valueOf(vpd.get("var"+(j+1))) : "";
				cell = getCell(sheet, i+1+start_header, j);
				cell.setCellStyle(contentStyle);
				setText(cell,varstr);
			}
			
		}
		
	}

    /**
     * 按照参数format的格式，日期转字符串
     *
     * @param date
     * @param format
     * @return
     */
    private String date2Str(Date date, String format)
    {
        if (date != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else
        {
            return "";
        }
    }

}
