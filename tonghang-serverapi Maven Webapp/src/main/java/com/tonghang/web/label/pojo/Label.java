package com.tonghang.web.label.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.user.pojo.User;

@Entity
@Table(name="labels")
public class Label implements Serializable{

	@Column(name="id")
	private int id;
	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	private String label_name;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date created_at=new Date();
	@ManyToMany(mappedBy="labellist")
	private Set<User> userlist;
//	@OneToMany(mappedBy="label")
//	private Set<Topic> topiclist;
	
	public Set<User> getUserlist() {
		return userlist;
	}
	public void setUserlist(Set<User> userlist) {
		this.userlist = userlist;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLabel_name() {
		return label_name;
	}
	public void setLabel_name(String label_name) {
		this.label_name = label_name;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	
}
