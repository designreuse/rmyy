package com.ewcms.system.scheduling.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ewcms.common.entity.BaseEntity;

/**
 * @author 吴智俊
 */
@Entity
@Table(name = "sys_job_class")
public class JobClass extends BaseEntity<Long> {

	private static final long serialVersionUID = -4428638031352721701L;

	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "class_name", nullable = false, unique = true)
	private String className;
	@Column(name = "description", columnDefinition = "text")
	private String description;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
