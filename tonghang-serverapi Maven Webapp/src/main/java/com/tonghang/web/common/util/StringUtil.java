package com.tonghang.web.common.util;

import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class StringUtil {

	/**
	 * 获取前台的RequestBody中的参数（用 ; 分割的字符串）
	 * @param obj
	 * @param index
	 * @return
	 */
	public static String getParameter(String obj,int index){
		String [] param = obj.split("\\;");
		System.out.println(param[index]);
		return param[index];
	}
	
	public static String randomCode(int level){
		String randCode = "abcdefghijklmnopqrstuvwxyz";
		String numberic = "1234567890";
		String password = "";
		Random rand = new Random();
		for(int i=0;i<level;i++){
			if(i<level/2){
				password = password + randCode.charAt(rand.nextInt(26));
			}else{
				password = password + numberic.charAt(rand.nextInt(10));
			}
		}
		return password;
	}
	
	/**
	 * TOKEN通过email生成
	 */
	public static String hmacSha1(String value, String key) {
        try {
            // Get an hmac_sha1 key from the raw key bytes
            byte[] keyBytes = key.getBytes();           
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);
            //  Covert array of Hex bytes to a String
            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
