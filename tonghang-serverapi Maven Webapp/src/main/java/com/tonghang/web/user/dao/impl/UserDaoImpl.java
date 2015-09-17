package com.tonghang.web.user.dao.impl;

import java.sql.DriverManager;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import net.rubyeye.xmemcached.MemcachedClient;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	MemcachedClient memcache;
	
	@Override
	public User findUserById(String client_id) {
		// TODO Auto-generated method stub
		System.out.println("走数据库方法：findUserById");
		User user = (User)sessionFactory.getCurrentSession().get(User.class, client_id);
		return user;
	}
	
	/**
	*notice:性别和年龄信息不全的用户不会被查出来
	*
	*/
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserByLabel(String label_name,int nowpage) {
		// TODO Auto-generated method stub
		System.out.println("走数据库方法：findUserByLabel");
		Query query = sessionFactory.getCurrentSession().createQuery("select distinct user from User as user left join user.labellist as " +
				"label where lower(label.label_name) like concat('%',lower(:label_name),'%') " +
				"and (user.birth is not null and user.birth != '') and (user.sex is not null and user.sex != '') order by user.created_at");
		List<User> user = (List<User>) query.setParameter("label_name", label_name).list();
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		User user = (User) sessionFactory.getCurrentSession().createQuery("from User as user where user.email = :email")
															.setParameter("email", email).uniqueResult();
		return user;
	}

	@Override
	public void save(User user) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(user);
	}

	@Override
	public void saveOrUpdate(User user) {
		// TODO Auto-generated method stub 
		sessionFactory.getCurrentSession().saveOrUpdate(user); 
	}
	/**
	 * 模糊搜索（findUserByNickName是精确的）.性别和年龄信息不全的用户不会被查出来
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserByUsername(String username,int page) {
		// TODO Auto-generated method stub 
		List<User> users = sessionFactory.getCurrentSession().createQuery("select distinct user from User as user where lower(username) like concat('%',lower(:username),'%') and (user.birth is not null and user.birth != '') and(user.sex is not null and user.sex != '') order by user.created_at").
							setParameter("username", username).setFirstResult(Constant.PAGESIZE*(page-1)).setMaxResults(Constant.PAGESIZE).list();
		return users;
	}

	@Override
	public void update(String client_id, User user) {
		// TODO Auto-generated method stub
		User u =  (User)sessionFactory.getCurrentSession().get(User.class, client_id);
		u = user;
		sessionFactory.getCurrentSession().update(u);
	}

	@Override
	public void deleteLabel(String client_id,Label label) {
		// TODO Auto-generated method stub
		User user = (User) sessionFactory.getCurrentSession().get(client_id,User.class);
		user.getLabellist().remove(label);
		sessionFactory.getCurrentSession().update(user);
	}

	@Override
	public void deleteAllLabel(String client_id) {
		// TODO Auto-generated method stub
		User user = (User) sessionFactory.getCurrentSession().get(User.class,client_id);
		user.setLabellist(new HashSet<Label>());
		sessionFactory.getCurrentSession().update(user);
	}

	@Override
	public void addFriend(User my, User friend) {
		// TODO Auto-generated method stub
		my.getFriends().add(friend);
		saveOrUpdate(my);
	}

	@Override
	public void deleteFriend(User my, User friend) {
		// TODO Auto-generated method stub
		my.getFriends().remove(friend);
		sessionFactory.getCurrentSession().update(my);
	}

	/**
	 * 精确搜索（findUserByUsername是模糊的）
	 */
	@Override
	public User findUserByNickName(String nickname) {
		// TODO Auto-generated method stub
		User user = (User) sessionFactory.getCurrentSession().createQuery("from User as user where user.username = :nickname")
																		.setParameter("nickname", nickname).uniqueResult();
		return user;
	}

	@Override
	public void addBlocker(User me, User blocker) {
		// TODO Auto-generated method stub
		me.getBlacklist().add(blocker);
		sessionFactory.getCurrentSession().update(me);
	}

	@Override
	public void deleteBlock(User me, User blcoker) {
		// TODO Auto-generated method stub
		me.getBlacklist().remove(blcoker);
		sessionFactory.getCurrentSession().update(me);
	}

	@Override
	public List<User> findAllUser() {
		// TODO Auto-generated method stub
		return sessionFactory.getCurrentSession().createQuery("from User").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findOneUserByCreatedAtDesc(int begin,int end) {
		// TODO Auto-generated method stub
		return sessionFactory.getCurrentSession().createQuery("from User as user where (user.birth is not null and user.birth != '') and(user.sex is not null and user.sex != '') order by user.created_at desc")
															.setFirstResult(begin).setMaxResults(end).list();
	}

	@Override
	public List<User> findUserByUsernameUnique(String username) {
		// TODO Auto-generated method stub
		List<User> users = sessionFactory.getCurrentSession().createQuery("from User as user where user.username = :username").setParameter("username", username).list();
		return users;
	}

}
