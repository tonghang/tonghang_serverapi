package com.tonghang.web.common.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class JPushUtil {
	/**
	 * 
	 * @param id		client_id
	 * @param name	用户名
	 * @param type	推送类型
	 */
	public static void push(String to_id,String from_id,String name,String type,String message){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("Authorization","Basic " + "ZWI0ZTc5YzRhYjE4MmQ3MjVlYzJmZjE1OmVkMzIxNjdhODY0MWFiMWVlODY1OGIzYQ==");
		Map<String, Object> parts = new HashMap<String, Object>();
		parts.put("platform","all");
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		list.add(to_id);
		map1.put("alias", list);
		parts.put("audience", map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", from_id);
		map2.put("name", name);
		map2.put("type", type);
		map2.put("message", message);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("msg_content", map2);
		parts.put("message", map3);
		try {
			DataUtil.postEntity("https://api.jpush.cn/v3/push", new HttpEntity(parts,headers),Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	
	public static void pushList(List<String> to_ids,String from_id,String name,String type,String message){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("Authorization","Basic " + "ZWI0ZTc5YzRhYjE4MmQ3MjVlYzJmZjE1OmVkMzIxNjdhODY0MWFiMWVlODY1OGIzYQ==");
		Map<String, Object> parts = new HashMap<String, Object>();
		parts.put("platform","all");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("alias", to_ids);
		parts.put("audience", map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", from_id);
		map2.put("name", name);
		map2.put("type", type);
		map2.put("message", message);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("msg_content", map2);
		parts.put("message", map3);
		try {
			DataUtil.postEntity("https://api.jpush.cn/v3/push", new HttpEntity(parts,headers),Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
}
