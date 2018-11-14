package com.ewcms.yjk.sb.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.common.Constants;
import com.ewcms.common.entity.search.SearchParameter;
import com.ewcms.common.web.controller.BaseCRUDController;
import com.ewcms.common.web.validate.AjaxResponse;
import com.ewcms.security.user.entity.User;
import com.ewcms.security.user.web.bind.annotation.CurrentUser;
import com.ewcms.yjk.sb.entity.DrugForm;
import com.ewcms.yjk.sb.entity.AuditStatusEnum;
import com.ewcms.yjk.sb.service.DrugFormService;
import com.ewcms.yjk.sp.entity.SystemParameter;
import com.ewcms.yjk.sp.service.SystemParameterService;
import com.ewcms.yjk.zd.commonname.entity.CommonNameContents;
import com.ewcms.yjk.zd.commonname.service.CommonNameContentsService;
import com.ewcms.yjk.zd.commonname.service.CommonNameRuleService;
import com.ewcms.yjk.zd.commonname.service.CommonNameService;

/**
 * @author zhoudongchu
 */
@Controller
@RequestMapping(value = "/yjk/sb/drugform")
public class DrugFormController extends BaseCRUDController<DrugForm, Long> {
	@Autowired
	private CommonNameRuleService commonNameRuleService;
	@Autowired
	private SystemParameterService systemParameterService;
	@Autowired
	private CommonNameContentsService commonNameContentsService;
	@Autowired
	private CommonNameService commonNameService;

	private DrugFormService getDrugFormService() {
		return (DrugFormService) baseService;
	}

	public DrugFormController() {
		setListAlsoSetCommonData(true);
		setResourceIdentity("sb:drugform");
	}

	@Override
	protected void setCommonData(Model model) {
		super.setCommonData(model);
		model.addAttribute("isOpenDeclare", systemParameterService.isOpenDrugDeclare());
		if (systemParameterService.isOpenDrugDeclare()) {
			SystemParameter systemParameter = systemParameterService.findByEnabledTrue();
			model.addAttribute("declareRule", "申报规则：一品两规限数为" + systemParameter.getDeclarationLimt() + "，最大药品申报数量为"
					+ systemParameter.getDeclareTotalLimt());
		}
		model.addAttribute("stateList", AuditStatusEnum.values());
		model.addAttribute("commonNameRuleList",
				commonNameRuleService.findByDeletedFalseAndEnabledTrueOrderByWeightAsc());
	}

	@RequestMapping(value = "querybyuser")
	@ResponseBody
	public Map<String, Object> queryByUser(@CurrentUser User user,
			@ModelAttribute SearchParameter<Long> searchParameter, Model model) {
		searchParameter.getSorts().put("auditStatus", Direction.DESC);
		searchParameter.getSorts().put("id", Direction.DESC);
		searchParameter.getParameters().put("EQ_userId", user.getId());
		Map<String, Object> queryObj = super.query(searchParameter, model);
		return queryObj;
	}

	@RequestMapping(value = "save/discard")
	@Override
	public String save(Model model, DrugForm m, BindingResult result, List<Long> selections) {
		throw new RuntimeException("discarded method");
	}
	/**
	 * 新药填写，满足一品两规的才能填写入库
	 * 
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@CurrentUser User user, Model model, @Valid @ModelAttribute("m") DrugForm m,
			BindingResult result, @RequestParam(required = false) List<Long> selections,
			RedirectAttributes redirectAttributes) {
		if (hasError(m, result)) {
			return showSaveForm(model, selections);
		}

		setCommonData(model);

		if (permissionList != null) {
			this.permissionList.assertHasCreatePermission();
		}
		CommonNameContents vo = m.getCommonNameContents();
		if (vo == null || vo.getId() == null) {
				redirectAttributes.addFlashAttribute(Constants.MESSAGE, "数据输入不完整，请重新输入");
				return redirectToUrl(viewName("save"));

		}
		String declareResult = getDrugFormService().drugDeclare(user, m);

		if (!declareResult.equals("false")) {
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, declareResult);
			
		} else {
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新药填写成功！");
		}
		return redirectToUrl(viewName("save"));
	}
	

	@RequestMapping(value = "{drugFormId}/deletedeclare")
	@ResponseBody
	public AjaxResponse restoreCommonName(@PathVariable(value = "drugFormId") Long drugFormId) {
		AjaxResponse ajaxResponse = new AjaxResponse("删除成功");
		try {
			getDrugFormService().delete(drugFormId);
		} catch (IllegalStateException e) {
			ajaxResponse.setSuccess(Boolean.FALSE);
			ajaxResponse.setMessage("删除失败");
		}
		return ajaxResponse;
	}


	@RequestMapping(value = "/declaresubmit")
	public String declareSubmit(Model model) {
		model.addAttribute("commonNameRuleList",
				commonNameRuleService.findByDeletedFalseAndEnabledTrueOrderByWeightAsc());
		return viewName("declare");
	}
	/**
	 * 批量对填写新药进行申报，同时要满足一品量规和申报上限的才可入库
	 * 
	 */
	@RequestMapping(value = "savedeclaresubmit")
	@ResponseBody
	public AjaxResponse saveDeclareSubmit(@RequestParam(required = false) List<Long> selections) {
		AjaxResponse ajaxResponse = new AjaxResponse("申报提交成功！");

		try {
			if (selections != null && !selections.isEmpty()) {
				String noDeclareCommonName = getDrugFormService().saveDeclareSubmit(selections);
				if (noDeclareCommonName != null && noDeclareCommonName.length() > 0) {
					ajaxResponse.setMessage("以下药品:" + noDeclareCommonName + "因超过申报限数或总数，未能申报！");
				}
			}
		} catch (IllegalStateException e) {
			ajaxResponse.setSuccess(Boolean.FALSE);
			ajaxResponse.setMessage("申报提交失败了！");
		}
		return ajaxResponse;
	}
	/**
	 * 查询登录系统的医生所有未申报所填写的新药
	 * 
	 */
	@RequestMapping(value = "/querydeclaresubmit")
	@ResponseBody
	public Map<String, Object> queryDeclareSubmit(@CurrentUser User user, Model model) {
		List<DrugForm> drugFormList = getDrugFormService().findByUserIdAndDeclaredFalse(user.getId());
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if (drugFormList != null) {
			queryMap.put("total", drugFormList.size());
			queryMap.put("rows", drugFormList);
		}
		return queryMap;
	}

	@RequestMapping(value = "/declarecancel")
	public String declareCancel(Model model) {
		model.addAttribute("commonNameRuleList",
				commonNameRuleService.findByDeletedFalseAndEnabledTrueOrderByWeightAsc());
		return viewName("cancel");
	}
	/**
	 * 批量撤销还未初审的申报药品
	 * 
	 */
	@RequestMapping(value = "savedeclarecancel")
	@ResponseBody
	public AjaxResponse saveDeclareCancel(@RequestParam(required = false) List<Long> selections) {
		AjaxResponse ajaxResponse = new AjaxResponse("申报撤销成功！");

		try {
			if (selections != null && !selections.isEmpty()) {
				getDrugFormService().saveDeclareCancel(selections);
			}
		} catch (IllegalStateException e) {
			ajaxResponse.setSuccess(Boolean.FALSE);
			ajaxResponse.setMessage("申报撤销失败了！");
		}
		return ajaxResponse;
	}
	/**
	 * 查询当前登录系统医生未初审了的所有申报药品
	 * 
	 */
	@RequestMapping(value = "/querydeclarecancel")
	@ResponseBody
	public Map<String, Object> queryDeclareCancel(@CurrentUser User user, Model model) {
		List<DrugForm> drugFormList = getDrugFormService().findByUserIdAndAuditStatus(user.getId(),
				AuditStatusEnum.init);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if (drugFormList != null) {
			queryMap.put("total", drugFormList.size());
			queryMap.put("rows", drugFormList);
		}
		return queryMap;
	}

//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/querydeclare")
//	@ResponseBody
//	public List<CommonNameContents> queryDeclare(@ModelAttribute SearchParameter<Long> searchParameter, Model model) {
//		searchParameter.getSorts().put("updateDate", Direction.DESC);
//		searchParameter.getParameters().put("EQ_deleted", Boolean.FALSE);
//		Map<String, Object> map = super.query(searchParameter, model);
//		return DuplicateRemovalUtil.removeDuplicateOrder((List<CommonNameContents>) map.get("rows"),
//				commonNameRuleService.findByDeletedFalseAndEnabledTrueOrderByWeightAsc()
//						.get(Integer.parseInt(searchParameter.getParameters().get("objIndex").toString()))
//						.getRuleName());
//	}

}
