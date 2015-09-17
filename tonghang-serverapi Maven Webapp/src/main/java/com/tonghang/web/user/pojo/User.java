package com.tonghang.web.user.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Component;

import com.tonghang.web.common.pojo.FeedBack;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.statistics.pojo.Channel;
import com.tonghang.web.topic.pojo.Topic;

/**
 * 
 * @author Administrator 用户信息包括：id 账号(number) 用户名(id) 密码(password) email
 *         电话号(phone) 性别(sex) 生日(birth) 状态(正常或封号) 是否在线(isonline) 所在省份(city),地市级(ext1,ext2)
 *         头像(image) 创建时间(created_time) 最近登录时间(last_login_time)关联属性(labellist:用户包含的标签，topic:用户加入的话题)
 * 			关联属性均由多的地方管理，所以user_topics关系由Topic类管理
 */
@Component("user")
@Entity
@Table(name="users")
public class User implements Serializable{
	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	private String client_id;

	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="email")
	private String email;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="sex")
	private String sex;
	
	@Column(name="birth")
	private String birth;
	
	@Column(name="status")
	public String status;
	
	@Column(name="isonline")
	private String isonline;
	
	@Column(name="province")
	private String province;
	
	@Column(name="city")
	private String city;
	
	@ManyToOne()
	@JoinColumn(name="channel_id")
	private Channel channel;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at",updatable=true)
	private Date created_at = new Date();
	
	@Column(name="last_login_at")
	private Date last_login_at;
	
	@ManyToMany()
	@JoinTable(name = "users_labels",
	   joinColumns = @JoinColumn(name = "client_id"),
	   inverseJoinColumns = @JoinColumn(name = "label_name"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Label> labellist;
	
	@ManyToOne()
	@JoinTable(name = "user_topics",
	   joinColumns = @JoinColumn(name = "client_id"),
	   inverseJoinColumns = @JoinColumn(name = "huanxin_group_id"))
	private Topic topic;
	
	@ManyToMany()
	@JoinTable(name="friends",
		joinColumns=@JoinColumn(name="client_id"),
		inverseJoinColumns=@JoinColumn(name="friend_id"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<User> friends;
	
	@OneToMany(mappedBy="user")
	private Set<FeedBack> feedbacks;
	
	@ManyToMany()
	@JoinTable(name="blacklist",
		joinColumns=@JoinColumn(name="client_id"),
		inverseJoinColumns=@JoinColumn(name="blocker_id"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<User> blacklist;
	
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public Set<Label> getLabellist() {
		return labellist;
	}
	public void setLabellist(Set<Label> labellist) {
		this.labellist = labellist;
	}
	
	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsonline() {
		return isonline;
	}

	public void setIsonline(String isonline) {
		this.isonline = isonline;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}

	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getLast_login_at() {
		return last_login_at;
	}

	public void setLast_login_at(Date last_login_at) {
		this.last_login_at = last_login_at;
	}
	public Set<User> getFriends() {
		return friends;
	}
	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}
	public Set<FeedBack> getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(Set<FeedBack> feedbacks) {
		this.feedbacks = feedbacks;
	}
	
	public Set<User> getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(Set<User> blacklist) {
		this.blacklist = blacklist;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((client_id == null) ? 0 : client_id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (client_id == null) {
			if (other.client_id != null)
				return false;
		} else if (!client_id.equals(other.client_id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "User [client_id=" + client_id + ", username=" + username
				+ ", labellist=" + labellist+ "]";
	}
	
}
