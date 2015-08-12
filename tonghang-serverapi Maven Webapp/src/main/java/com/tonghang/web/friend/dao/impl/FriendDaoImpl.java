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
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		session.saveOrUpdate(invitation);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void deleteInvitation(Invitation invitation) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		session.delete(invitation);
		session.getTransaction().commit();
		session.close();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Invitation> getInvitationByUserId(String client_id) {
		// TODO Auto-generated method stub   from Invitation as invitation inner join invitation.invitee as invitee where invitee.client_id = :client_id
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		System.out.println("getInvitationByUserId:"+client_id);
		List<Invitation> invitations = session.createQuery("from Invitation as invitation where invitation.invitee.client_id = :client_id").setParameter("client_id", client_id).list();
		session.close();
		return invitations;
	}

	@Override
	public boolean isFriend(String my_id, String friend_id) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		User my = (User) session.get(User.class, my_id);
		User friend = (User) session.get(User.class, friend_id);
		boolean flag = my.getFriends().contains(friend);
		session.close();
		return flag;
	}

}
