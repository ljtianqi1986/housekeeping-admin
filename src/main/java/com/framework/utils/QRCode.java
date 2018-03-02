package com.framework.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

public class QRCode {
	
	/**
	 * 图片和文字
	 * @param
	 * @return
	 */
	public static void graphicsGeneration(BufferedImage bimg,String shop_name,String money, String remark,
			String imgurl,String file_name) throws IOException {

		int imageWidth = 4000;// 图片的宽度
		int imageHeight = 3000;// 图片的高度

		BufferedImage image = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		 /* 消除java.awt.Font字体的锯齿 */  
		// 设置“抗锯齿”的属性  
		graphics.fillRect(0, 0, imageWidth, imageHeight);
		
		 //设置文字颜色  
		graphics.setColor(Color.BLACK);
		 //改变字体大小
		graphics.setFont(new Font("宋体",Font.BOLD,150));   
		//设置文字内容、位置  
		int wheight = 2400;
		
		//二维码
		if (bimg != null){
			graphics.drawImage(bimg, 0, 200, 4000,2400,null);
		}
		
		//店名
		if(StringUtils.isNotBlank(shop_name)){
			drawString(graphics,"店名:" + shop_name,imageWidth,250);
		}
		
		//金额
		if(StringUtils.isNotBlank(money)){
			wheight = wheight+250;
			drawString(graphics,"金额:" + money+"元",imageWidth,wheight);
		}
	    
		//备注
		if(StringUtils.isNotBlank(remark)){
			wheight = wheight+250;
			drawString(graphics,remark,imageWidth,wheight);
		}
		
		graphics.dispose();

		File file = new File(imgurl);
		ImageIO.write(image, "png", file);
		OssUtils.UploadSmallFile(file,file_name);
		file.delete();
	}

	/**
	 * 图片文字居中
	 * @param g
	 * @param str
	 * @param
	 * @param yPos
	 */
	public static void drawString(Graphics g, String str, int imageWidth, int yPos) {
        int strWidth = g.getFontMetrics().stringWidth(str);
        g.drawString(str, imageWidth/2 - strWidth / 2, yPos);
	}
	
	/**
	 * 生成二维码图片<br>
	 * @param
	 * @return
	 */
	public static BufferedImage getencodeimg(String contents,String imgPath, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) {
		Hashtable<EncodeHintType, Object> hint = new Hashtable<EncodeHintType, Object>();  
        // 注意要使用 utf-8，因为刚才生成二维码时，使用了utf-8  
		hint.put(EncodeHintType.CHARACTER_SET, "utf-8");  
		BufferedImage image = null;
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, format, width, height,hint);
			image = toBufferedImage(bitMatrix);
			if (imgPath == null || "".equals(imgPath)) {  
		       return image;  
		    }  
	        // 插入图片  
	        insertImage(image, imgPath,width,height);  
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return image;
	}
	
	/** 
     * 插入LOGO 
     *  
     * @param source 
     *            二维码图片 
     * @param imgPath 
     *            LOGO图片地址 
     * @param
     *
     * @throws Exception 
     */  
    private static void insertImage(BufferedImage source, String imgPath,int img_width,int img_height) throws Exception {
		URL url = new URL(imgPath);
		//打开链接
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		//设置请求方式为"GET"
		conn.setRequestMethod("GET");
		//超时响应时间为5秒
		conn.setConnectTimeout(5 * 1000);
		//通过输入流获取图片数据
		InputStream inStream = conn.getInputStream();
		//得到图片的二进制数据，以二进制封装得到数据，具有通用性
		byte[] data = readInputStream(inStream);
		//new一个文件对象用来保存图片，默认保存当前工程根目录
		File file = new File("logo_tem.jpg");
		//创建输出流
		FileOutputStream outStream = new FileOutputStream(file);
		//写入数据
		outStream.write(data);
		//关闭输出流
		outStream.close();
		//File file = new File(imgPath);
        if (!file.exists()) {  
            System.err.println(""+imgPath+"   该文件不存在！");  
            return;  
        }  
        Image src = ImageIO.read(file);
//        int width = src.getWidth(null);  
//        int height = src.getHeight(null);  
//        if (width > 300) {  
//            width = 300;  
//        }  
//        if (height > 300) {  
//            height = 300;  
//        }  
        int width = 500;  
        int height = 500;  
        Image image = src.getScaledInstance(width, height,  
                Image.SCALE_SMOOTH);  
        BufferedImage tag = new BufferedImage(width, height,  
                BufferedImage.TYPE_INT_RGB);  
        Graphics g = tag.getGraphics();  
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
        g.dispose();  
        src = image;  
        // 插入LOGO  
        Graphics2D graph = source.createGraphics();  
        int x = (img_width - width) / 2;  
        int y = (img_height - height) / 2;  
        graph.drawImage(src, x, y, width, height, null);  
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);  
        graph.setStroke(new BasicStroke(3f));  
        graph.draw(shape);  
        graph.dispose();
		file.delete();
    }

	public static byte[] readInputStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		//创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		//每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		//使用一个输入流从buffer里把数据读取出来
		while( (len=inStream.read(buffer)) != -1 ){
			//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		//关闭输入流
		inStream.close();
		//把outStream里的数据写入内存
		return outStream.toByteArray();
	}
	/**
	 * 生成二维码内容<br>
	 * @param matrix
	 * @return
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) == true ? 0xff000000 : 0xFFFFFFFF);
			}
		}
		return image;
	}
}
