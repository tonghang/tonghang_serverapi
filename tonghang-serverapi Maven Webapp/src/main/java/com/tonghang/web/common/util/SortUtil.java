package com.tonghang.web.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 对外提供方法  sortByLabelName(List<Map>,List<String>)
 * 排序需要一个List<Map>,map代表一个用户，List<Map>则代表要排序的多个用户。
 * 排序还需要一个标签列表List<String>，表示目标用户包含哪些标签。
 * @author Administrator
 *
 */
public class SortUtil{

	/**
	 * 业务功能：遍历所有的推荐用户，检查每一个推荐用户的标签和自己的标签有几个是一样（类似）的，然后根据一样标签的数量排序
	 * @param list
	 * @param objlabels 用户数据列表 Map<String,Object>表示用户数据
	 * @return
	 */
	public static List<Map<String,Object>> sortByLabelName(List<Map<String,Object>> list,List<String> objlabels){
		int index = 0;
		Map<String,Object> pack =new HashMap<String,Object>();
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		List<Integer> number = new ArrayList<Integer>();
		for(Map<String,Object> map:list){
			List<String> labels = (List<String>)map.get("labels");
			int nums = howManySameLabel(objlabels, labels);
			map.put("samelabel",nums);
			System.out.println("用户"+map.get("username")+"和我一样的标签有"+nums+"个");
			result.add(map);
		}
		return sortNumber(result);
	}
	/**
	 * 数一下当前用户的标签和用户的标签有多少一样的
	 * @param objlabels 用户的标签
	 * @param srclabels	 要推荐的用户所持有的标签
	 * @return
	 */
	private static int howManySameLabel(List<String> objlabels,List<String> srclabels){
		int count = 0;
		int result = 0;
		int index = 0;
		List<String> temp = new ArrayList<String>();
		if(objlabels==null||objlabels.size()<=0){
			return 0;
		}
		if(objlabels.size()>srclabels.size()){
			temp =objlabels;
			objlabels = srclabels;
			srclabels = temp;
		}
		for(String obj:objlabels){
			for(int i=0;i<srclabels.size();i++){
				if(srclabels.get(i).trim().equalsIgnoreCase(obj.trim())||srclabels.get(i).trim().contains(obj.trim())){
					result ++;
				}
			}
		}
		return result;
	}
	
	/**
	 * Map中存的是用户数据还有一个samelabel ，samelabel表示相同标签的数量，根据这个数量为用户排序。
	 * @param list
	 * @return
	 */
	private static List<Map<String,Object>> sortNumber(List<Map<String,Object>> list){
		int index = 0;
		int max = 0;
		Map<String,Object> temp = new HashMap<String, Object>();
		for(int i=0;i<list.size();i++){
			for(int j=0;j<list.size()-i-1;j++){
				Map<String,Object> pre = (Map<String, Object>) list.get(j);
				Map<String,Object> next = (Map<String, Object>) list.get(j+1);
				if((Integer)pre.get("samelabel")<(Integer)next.get("samelabel")){
					temp = pre;
					list.set(j, next);
					list.set(j+1,pre);
				}
			}
		}
		return list;
		
	}

}
