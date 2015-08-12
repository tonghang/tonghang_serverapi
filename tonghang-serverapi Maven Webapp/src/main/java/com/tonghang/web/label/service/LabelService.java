package com.tonghang.web.label.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.label.dao.LabelDao;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.pojo.User;

@Service("labelService")
public class LabelService {

	private LabelDao labelDao;

	public LabelDao getLabelDao() {
		return labelDao;
	}
	@Resource(name="labelDao")
	public void setLabelDao(LabelDao labelDao) {
		this.labelDao = labelDao;
	}
	
//	public List<Map<String,List<Map<String,Object>>>> findLabelByUser(User user){
//		List<Label> labels = labelDao.findLabelByUser(user);
//		List<Map<String,List<Map<String,Object>>>> result = new ArrayList<Map<String,List<Map<String,Object>>>>();
//		Map<String,List<Map<String,Object>>> usermap = new HashMap<String, List<Map<String,Object>>>();
//		 List<Map<String,Object>> users = new ArrayList<Map<String,Object>>();
//		for(Label l:labels){
//			Map<String,Object> msg = new HashMap<String, Object>();
//		}
//	}
	
}
