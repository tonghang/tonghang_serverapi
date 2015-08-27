package com.tonghang.web.common.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="system_config")
public class SystemConfig {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(name="can_register_user")
	private int can_regist;
	
	@Column(name="can_login")
	private int can_login;
	
	@Column(name="can_upgrade_silently")
	private int can_upgrade_silence;

	@Column(name="use_adv")
	private int use_adv;
	@Column(name="third_adv")
	private int third_adv;
	@Column(name="self_adv_url")
	private String self_adv_url;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCan_regist() {
		return can_regist;
	}

	public void setCan_regist(int can_regist) {
		this.can_regist = can_regist;
	}

	public int getCan_login() {
		return can_login;
	}

	public void setCan_login(int can_login) {
		this.can_login = can_login;
	}

	public int getCan_upgrade_silence() {
		return can_upgrade_silence;
	}

	public void setCan_upgrade_silence(int can_upgrade_silence) {
		this.can_upgrade_silence = can_upgrade_silence;
	}

	public int getThird_adv() {
		return third_adv;
	}

	public void setThird_adv(int third_adv) {
		this.third_adv = third_adv;
	}

	public String getSelf_adv_url() {
		return self_adv_url;
	}

	public void setSelf_adv_url(String self_adv_url) {
		this.self_adv_url = self_adv_url;
	}

	public int getUse_adv() {
		return use_adv;
	}

	public void setUse_adv(int use_adv) {
		this.use_adv = use_adv;
	}
	
	
}
