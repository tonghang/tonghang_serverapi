package com.tonghang.web.common.util;

import java.security.MessageDigest;
import java.util.UUID;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonghang.web.label.pojo.Label;

public class SecurityUtil {

	/**
	 * 获得UUID,并去掉其中的 ‘-’ 字符
	 * @return
	 */
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
	
	 /**
     * 生成md5
     * @param message
     * @return
     */
    public static String getMD5(Object message) {
        String md5str = "";
        try {
            //1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            //2 将消息变成byte数组
            ObjectMapper mapper = new ObjectMapper();
            String s = mapper.writeValueAsString(message);
            System.out.println(s);
//            MappingJackson2HttpMessageConverter jackson = new MappingJackson2HttpMessageConverter();
            byte[] input = s.getBytes();
            //3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(input);
 
            //4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5str = bytesToHex(buff);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }
 

    
    /**
     * 二进制转十六进制
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        //把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
             digital = bytes[i];
 
            if(digital < 0) {
                digital += 256;
            }
            if(digital < 16){
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }
}
