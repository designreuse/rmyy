package com.ewcms.empi.card.manage.web.controller;

import java.util.Map;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.common.entity.search.SearchParameter;
import com.ewcms.common.web.controller.BaseCRUDController;
import com.ewcms.empi.card.manage.entity.PracticeCardHistory;

/**
 *@author zhoudongchu
 */
@Controller
@RequestMapping(value = "/empi/card/manage/practicecardhistory")
public class PracticeCardHistoryController extends BaseCRUDController<PracticeCardHistory, Long> {
	@RequestMapping(value = "{practiceCardId}/detail")
	public String index(@PathVariable(value = "practiceCardId")Long practiceCardId, Model model){
		return this.viewName("detail");
	}
	
	@RequestMapping(value = "{practiceCardId}/query")
	@ResponseBody
	public Map<String, Object> query(@ModelAttribute SearchParameter<Long> searchParameter, @PathVariable(value = "practiceCardId")Long practiceCardId, Model model){
		searchParameter.getParameters().put("EQ_practiceCard.id", practiceCardId);
		searchParameter.getSorts().put("id", Direction.DESC);
		return super.query(searchParameter, model);
	}
}
