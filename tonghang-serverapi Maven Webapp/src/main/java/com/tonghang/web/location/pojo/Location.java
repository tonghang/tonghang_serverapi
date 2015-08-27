package com.tonghang.web.location.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.tonghang.web.user.pojo.User;

@Entity
@Table(name="location")
public class Location implements Serializable{

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@OneToOne()
	@JoinColumn(name="client_id")
	private User user;
	@Column(name="x_point")
	private double x_point;
	@Column(name="y_point")
	private double y_point;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public double getX_point() {
		return x_point;
	}
	public void setX_point(double x_point) {
		this.x_point = x_point;
	}
	public double getY_point() {
		return y_point;
	}
	public void setY_point(double y_point) {
		this.y_point = y_point;
	}
	
}
