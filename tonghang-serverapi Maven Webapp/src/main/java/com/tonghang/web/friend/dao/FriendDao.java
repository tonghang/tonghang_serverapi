package com.tonghang.web.friend.dao;

import java.util.List;

import com.tonghang.web.friend.pojo.Invitation;

public interface FriendDao {

	public void saveInvitation(Invitation invitation);
	public void deleteInvitation(Invitation invitation);
	
	public List<Invitation> getInvitationByUserId(String client_id);
	public boolean isFriend(String my,String friend);
}
