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
	
	
}
