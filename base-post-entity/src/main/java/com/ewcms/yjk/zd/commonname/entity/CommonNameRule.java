package com.ewcms.yjk.zd.commonname.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.entity.BaseSequenceEntity;
import com.ewcms.common.plugin.entity.LogicDeleteable;
import com.ewcms.common.plugin.entity.Movable;

/**
 * 药品匹配规则
 * <ul>
 * <li>ruleName:规则名(字段名)</li>
 * <li>ruleCnName:规则名(中文名)</li>
 * <li>weight:排序</li>
 * <li>enabled:是否启用</li>
 * <li>deleted:是否删除</li>
 * </ul>
 * 
 * @author wuzhijun
 *
 */
@Entity
@Table(name = "zd_common_name_rule")
@SequenceGenerator(name="seq", sequenceName="seq_zd_common_name_rule_id", allocationSize = 1)
public class CommonNameRule extends BaseSequenceEntity<Long> implements Movable, LogicDeleteable {
	private static final long serialVersionUID = -1670134352959536427L;

	@Column(name = "rule_name", nullable = false, unique=true)
	private String ruleName;
	
	@Column(name = "rule_cn_name", nullable = false, unique=true)
	private String ruleCnName;
	
	@Column(name = "weight")
	private Integer weight;
	
	@Column(name = "is_enabled")
	private Boolean enabled = Boolean.FALSE;
	
	@Column(name = "is_deleted")
    private Boolean deleted = Boolean.FALSE;
	
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleCnName() {
		return ruleCnName;
	}

	public void setRuleCnName(String ruleCnName) {
		this.ruleCnName = ruleCnName;
	}

	@Override
    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
    public void markDeleted() {
        this.deleted = Boolean.TRUE;
    }

	@Override
	public Integer getWeight() {
		return weight;
	}

	@Override
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}
