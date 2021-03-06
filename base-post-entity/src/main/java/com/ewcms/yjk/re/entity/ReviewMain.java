package com.ewcms.yjk.re.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.entity.BaseSequenceEntity;
import com.ewcms.common.utils.Collections3;
import com.ewcms.common.utils.EmptyUtil;
import com.ewcms.security.user.entity.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 评审主表
 * 
 * @author wuzhijun
 *
 */
@Entity
@Table(name = "re_review_main")
@SequenceGenerator(name = "seq", sequenceName = "seq_re_review_main_id", allocationSize = 1)
public class ReviewMain extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -439118229004524195L;

	@Column(name = "name", nullable = false, unique = true)
	private String name;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_date", columnDefinition = "Timestamp default CURRENT_TIMESTAMP", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Column(name = "remark", columnDefinition = "text")
	private String remark;
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.EAGER, targetEntity = ReviewExpert.class, mappedBy = "reviewMain", orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	@Basic(optional = true, fetch = FetchType.EAGER)
	@OrderBy("weight")
	private List<ReviewExpert> reviewExperts;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "extract_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date extractDate;
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "re_review_main_user", joinColumns = {
			@JoinColumn(name = "review_main_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "user_id", referencedColumnName = "id") }, uniqueConstraints = {@UniqueConstraint(columnNames = {
							"user_id", "review_main_id" })})
	private List<User> users;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@JSONField(serialize = false)
	public List<ReviewExpert> getReviewExperts() {
		return (reviewExperts == null) ? Lists.<ReviewExpert>newArrayList() : reviewExperts;
	}

	public void addReviewExpert(ReviewExpert reviewExpert) {
		reviewExpert.setReviewMain(this);
		getReviewExperts().add(reviewExpert);
	}
	
	public void setReviewExperts(List<ReviewExpert> reviewExperts) {
		this.reviewExperts = reviewExperts;
	}

	@JSONField(serialize = false)
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getUserNames() {
		return (EmptyUtil.isCollectionNotEmpty(users))
				? Collections3.convertToString(Collections3.extractToList(users, "username"), "/")
				: "";
	}

	@SuppressWarnings("unchecked")
	public Set<Long> getUserIds() {
		return (EmptyUtil.isCollectionNotEmpty(users)) ? Collections3.extractToSet(users, "id")
				: Sets.newHashSet();
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getExtractDate() {
		return extractDate;
	}

	public void setExtractDate(Date extractDate) {
		this.extractDate = extractDate;
	}
}
