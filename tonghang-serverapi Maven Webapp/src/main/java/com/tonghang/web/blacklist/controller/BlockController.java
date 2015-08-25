package com.tonghang.web.blacklist.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tonghang.web.blacklist.service.BlockService;

@Controller("blockController")
@RequestMapping("block")
public class BlockController {
	
	@Resource(name="blockService")
	private BlockService blockService;
	/**
	 * 业务功能：添加某用户到当前用户的黑名单列表中
	 * @param client_id		当前用户client_id
	 * @param blocker_id	被加入黑名单的用户client_id
	 * @return
	 */
	@RequestMapping("{client_id}/blocks/{blocker_id}")
	public ResponseEntity<Map<String,Object>> blcokUser(@PathVariable String client_id,@PathVariable String blocker_id) {
		Map<String,Object> result = blockService.createBlock(client_id, blocker_id);
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}
	/**
	 * 业务功能：把某用户从当前用户的黑名单中移除
	 * @param client_id		当前用户client_id
	 * @param blocker_id	被加入黑名单的用户client_id
	 * @return
	 */
	@RequestMapping(value="{client_id}/deblocks/{blocker_id}",method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> deblockUser(@PathVariable String client_id,@PathVariable String blocker_id) {
		Map<String,Object> result = blockService.deleteBlock(client_id, blocker_id);
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}
	/**
	 * 业务功能：查询某用户的黑名单列表
	 * @param client_id		当前用户client_id
	 * @return
	 */
	@RequestMapping(value="/{client_id}/blacklist",method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> getBlockerList(@PathVariable String client_id){
		Map<String,Object> result = blockService.findBlockList(client_id);
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}

}
