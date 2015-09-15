package com.tonghang.web.friend.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.friend.dao.FriendDao;
import com.tonghang.web.friend.pojo.Invitation;
import com.tonghang.web.user.pojo.User;

@Repository("friendDao")
public class FriendDaoImpl implements FriendDao {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Override
	public void saveInvitation(Invitation invitation) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(invitation);
	}

	@Override
	public void deleteInvitation(Invitation invitation) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().delete(invitation);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Invitation> getInvitationByUserId(String client_id) {
		// TODO Auto-generated method stub   from Invitation as invitation inner join invitation.invitee as invitee where invitee.client_id = :client_id
		return sessionFactory.getCurrentSession().createQuery("from Invitation as invitation where invitation.invitee.client_id = :client_id").setParameter("client_id", client_id).list();
	}

	@Override
	public boolean isFriend(String my_id, String friend_id) {
		// TODO Auto-generated method stub
		User my = (User) sessionFactory.getCurrentSession().get(User.class, my_id);
		User friend = (User) sessionFactory.getCurrentSession().get(User.class, friend_id);
		boolean flag = my.getFriends().contains(friend);
		return flag;
	}

}
