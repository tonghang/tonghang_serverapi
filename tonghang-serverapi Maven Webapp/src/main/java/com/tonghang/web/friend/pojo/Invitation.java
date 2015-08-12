package com.tonghang.web.friend.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.tonghang.web.user.pojo.User;


@Entity
@Table(name="invitations")
public class Invitation implements Serializable{

	private static final long serialVersionUID = -1125650621171025284L;

	@Column(name="id")
	private int iid;
	
	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@ManyToOne()
	@JoinColumn(name="inviter_id",columnDefinition="default null")
	private User inviter;
	
	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@ManyToOne()
	@JoinColumn(name="invitee_id",columnDefinition="default null")
	private User invitee;
	
	@Column(name="reason")
	private String reason;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date created_at=new Date();

	public int getId() {
		return iid;
	}

	public void setId(int iid) {
		this.iid = iid;
	}

	public User getInviter() {
		return inviter;
	}

	public void setInviter(User inviter) {
		this.inviter = inviter;
	}

	public User getInvitee() {
		return invitee;
	}

	public void setInvitee(User invitee) {
		this.invitee = invitee;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	
}
