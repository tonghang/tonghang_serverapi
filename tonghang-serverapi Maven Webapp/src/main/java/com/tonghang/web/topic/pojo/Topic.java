package com.tonghang.web.topic.pojo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.pojo.User;
@Deprecated
@Entity
@Table(name="topics")
/**
 * 
 * @author Administrator
 * notice:1.0/0.3 版本 已经删除话题功能
 *
 */
public class Topic {

	@Column(name="id")
	private int id ;
	@Column(name="subject")
	private String subject;
	@ManyToOne()
	@JoinColumn(name="label_name")
	private Label label;
	
	@OneToOne()
	@JoinColumn(name="client_id")
	private User user;
	
	@OneToMany(mappedBy="topic")
	private Set<User> users;
	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	private String huanxin_group_id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date created_at = new Date();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	
	public Set<User> getUsers() {
		return users;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public String getHuanxin_group_id() {
		return huanxin_group_id;
	}
	public void setHuanxin_group_id(String huanxin_group_id) {
		this.huanxin_group_id = huanxin_group_id;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	@Override
	public String toString() {
		return "Topic [id=" + id + ", subject=" + subject + ", label=" + label
				+ ", user=" + user + ", users=" + users + ", huanxin_group_id="
				+ huanxin_group_id + ", created_at=" + created_at + "]";
	}

	
}
