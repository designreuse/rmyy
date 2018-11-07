package com.ewcms.yjk.zd.commonname.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.common.repository.BaseRepository;
import com.ewcms.yjk.zd.commonname.entity.Administration;
import com.ewcms.yjk.zd.commonname.entity.CommonNameContents;

public interface CommonNameContentsRepository extends BaseRepository<CommonNameContents, Long> {
	@Query("select distinct c.common.administration from CommonNameContents c where c.common.commonName=?1 and c.deleted is false and c.declared is true")
	List<Administration> findAdministrationByCommonName(String commonName);
	
	List<CommonNameContents> findByCommonIdAndDeletedFalse(Long commonId);
	
	Page<CommonNameContents> findByCommonIdInAndDeletedFalse(List<Long> commonIds, Pageable pageable);
	
	List<CommonNameContents> findByCommonCommonNameAndCommonAdministrationIdAndDeletedFalseOrderByUpdateDateDesc(String commonName, Long administrationId);
}
