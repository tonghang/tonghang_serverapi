package com.tonghang.web.common.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class JPushUtil {
	
	
	public static void push(String id,String text){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("Authorization","Basic " + Constant.JPushBasic);
		Map<String, Object> parts = new HashMap<String, Object>();
		parts.put("platform","all");
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		list.add(id);
		map.put("alias", list);
		parts.put("audience", map);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("alert", text);
		parts.put("notification", m);
		DataUtil.postEntity("https://api.jpush.cn/v3/push", new HttpEntity(parts,headers),Map.class);
	}
}
