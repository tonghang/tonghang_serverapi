package com.tonghang.web.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartFile;


public class RequestUtil {

	/**
	 * 请求流中读取JSON字符串（目前暂时废弃）
	 * @param request
	 * @return
	 */
	@Deprecated
	public static String readRequest(HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader bufi = request.getReader();
			String line = null;
			while((line=bufi.readLine())!=null){
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}
	
	/**
	 * 业务功能：从请求中读取用户的头像
	 * @param request(请求对象，用来获取服务器路径)
	 * @param user(用户对象)
	 * @param picture(图片对象)
	 * 图片如果存在就覆盖掉，所有图片都叫sign.jpg
	 */
	public static void UserImageReceiver(HttpServletRequest request,String client_id, CommonsMultipartFile picture){
		if(picture!=null){
			String pictureRealPathDir = request.getSession().getServletContext().getRealPath("images");
			String fileName =pictureRealPathDir+File.separator+client_id+File.separator+Constant.IMAGE_NAME;              
			try {
				File f = new File(fileName);
				File folder = new File(pictureRealPathDir+File.separator+client_id);
				if(!folder.exists())
					folder.mkdirs();
				picture.getFileItem().write(f);
				LogUtil.printLog(f.getAbsolutePath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
//	public static void UserImageReceiver(HttpServletRequest request,String client_id, String picture){
//		System.out.println("UserImageReceiver:"+ picture);
//		if(picture!=null){
//			String pictureRealPathDir = request.getSession().getServletContext().getRealPath("image");
//			String fileName =pictureRealPathDir+File.separator+client_id+File.separator+Constant.IMAGE_NAME;
//			File folder = new File(pictureRealPathDir+File.separator+client_id);
//			if(!folder.exists())
//				folder.mkdirs();
//			try {
//				FileWriter out = new FileWriter(fileName);
//				char[] ch = picture.toCharArray();
//				for(int i=0;i<ch.length;i++){
//					out.write(ch[i]);
//				}
//				out.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
