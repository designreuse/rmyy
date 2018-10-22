package com.ewcms.yjk.sb.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ewcms.common.entity.search.SearchParameter;
import com.ewcms.common.web.controller.BaseCRUDController;
import com.ewcms.security.user.entity.User;
import com.ewcms.security.user.web.bind.annotation.CurrentUser;
import com.ewcms.yjk.sb.entity.DrugForm;
import com.ewcms.yjk.sb.service.DrugFormService;
import com.ewcms.yjk.zd.commonname.service.CommonNameRuleService;
import com.ewcms.yjk.zd.commonname.service.PillService;

/**
 *@author zhoudongchu
 */
@Controller
@RequestMapping(value = "/yjk/sb/drugform")
public class DrugFormController extends BaseCRUDController<DrugForm, Long> {
	@Autowired
	private CommonNameRuleService commonNameRuleService;
	
	private DrugFormService getDrugFormService() {
		return (DrugFormService) baseService;
	}
	
    @Override
    protected void setCommonData(Model model) {
        super.setCommonData(model);
        model.addAttribute("commonNameRuleList", commonNameRuleService.findRuleNameByDeleted());
    }
    
    public DrugFormController() {
        setResourceIdentity("yjk:drugform");
    }
	
    @RequestMapping(value = "query1")
	public Map<String, Object> query1(@CurrentUser User user,@ModelAttribute SearchParameter<Long> searchParameter, Model model){
		searchParameter.getSorts().put("id", Direction.DESC);
		if (!user.getAdmin()) {
			searchParameter.getParameters().put("EQ_userId", user.getId());
		}
		return super.query(searchParameter, model);
	}

    @RequestMapping(value = "save1", method = RequestMethod.POST)
	public String save1(@CurrentUser User user,Model model, DrugForm m, BindingResult result,
			List<Long> selections) {
		m.setUserId(user.getId());
		return super.save(model, m, result, selections);
	}
    
    
}
