<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<ewcms:head title="字典-大通用名总目录"/>
	<div id="edit-from" class="easyui-layout" data-options="fit:true" style="border:0;">
		<ewcms:showMessage/>
		<div data-options="region:'center',border:false">	
		 	<form:form id="editForm" method="post" action="${ctx}/yjk/zd/commonnamecontents/save" commandName="m"  class="form-horizontal">
		    	<ewcms:showGlobalError commandName="m"/>
		    	<form:hidden path="id"/>
		    	<c:forEach var="selection" items="${selections}">
	  				<input type="hidden" name="selections" value="${selection}" />
				</c:forEach>
			  	<table class="formtable">
		        	<tr>
						<td width="20%"><form:label path="common">通用名：</form:label></td>
						<td width="30%"><form:input path="common" cssClass="validate[required]" class="easyui-combox" data-options="valueField:'id',textField:'commonName',panelHeight:140,editable:true,height:50"/></td>
						<td width="20%"><form:label path="extractCommonName">提取通用名：</form:label></td>
						<td width="30%"><form:input path="extractCommonName"/></td>
					</tr>
		        	<tr>
						<td width="20%"><form:label path="projectName">项目名称：</form:label></td>
						<td width="30%"><form:input path="projectName"/></td>
						<td width="20%"><form:label path="batch">批次：</form:label></td>
						<td width="30%"><form:input path="batch"/></td>
					</tr>
		        	<tr>
						<td width="20%"><form:label path="source">来源：</form:label></td>
						<td width="30%">
						           <form:select path="source">
						              <form:option value="双信封目录">双信封目录</form:option>
						              <form:option value="医联体目录">医联体目录</form:option>
						              <form:option value="已审核待议价目录">已审核待议价目录</form:option>
						           </form:select>
						</td>
						<td width="20%"><form:label path="medicalDirNo">医保目录编号：</form:label></td>
						<td width="30%"><form:input path="medicalDirNo"/></td>
					</tr>
		        	<tr>
						<td width="20%"><form:label path="medicalDirName">医保目录药品名称：</form:label></td>
						<td width="30%"><form:input path="medicalDirName"/></td>
						<td width="20%"><form:label path="medicalDirPill">医保目录药品剂型：</form:label></td>
						<td width="30%"><form:input path="medicalDirPill"/></td>
					</tr>
		        	<tr>
						<td width="20%"><form:label path="medicalCategory">医保类别：</form:label></td>
						<td width="30%"><form:input path="medicalCategory"/></td>
						<td width="20%"><form:label path="medicalRemark">医保备注：</form:label></td>
						<td width="30%"><form:input path="medicalRemark"/></td>
					</tr>
		        	<tr>
						<td width="20%"><form:label path="drugType">药品类型：</form:label></td>
						<td width="30%"><form:input path="drugType"/></td>
						<td width="20%"><form:label path="drugRoute">给药途径：</form:label></td>
						<td width="30%"><form:input path="drugRoute"/></td>
					</tr>
		        	<tr>
						<td width="20%"><form:label path="pill">剂型：</form:label></td>
						<td width="30%"><form:input path="pill"/></td>
						<td width="20%"><form:label path="specNumber">规格*数量：</form:label></td>
						<td width="30%"><form:input path="specNumber"/></td>
					</tr>
		        	<tr>
						<td width="20%"><form:label path="productName">商品名：</form:label></td>
						<td width="30%"><form:input path="productName"/></td>
						<td width="20%"><form:label path="packageUnit">包装单位：</form:label></td>
						<td width="30%"><form:input path="packageUnit"/></td>
					</tr>
		        	<tr>
						<td width="20%"><form:label path="manufacturer">生产企业：</form:label></td>
						<td width="30%"><form:input path="manufacturer"/></td>
						<td width="20%"><form:label path="purchasePrice">采购价：</form:label></td>
						<td width="30%"><form:input path="purchasePrice"/></td>
					</tr>
		        	<tr>
						<td width="20%"><form:label path="packageMaterials">包材：</form:label></td>
						<td width="30%"><form:input path="packageMaterials"/></td>
						<td width="20%"><form:label path="minimalUnit">最小制剂单位：</form:label></td>
						<td width="30%"><form:input path="minimalUnit"/></td>
					</tr>
		        	<tr>
						<td width="20%"><form:label path="importEnterprise">进口企业：</form:label></td>
						<td width="30%"><form:input path="importEnterprise"/></td>
						<td width="20%"><form:label path="declared">是否允许申报：</form:label></td>
						<td width="30%"><form:checkbox path="declared"/></td>
					</tr>																																																	
				</table>
			</form:form>
		</div>
		<div data-options="region:'south'" style="text-align:center;height:30px;border:0">
	  		<a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:void(0);" onclick="javascript:$('#editForm').submit();">提交</a>
	  		<a class="easyui-linkbutton" data-options="iconCls:'icon-undo'" href="javascript:void(0);" onclick="javascript:$('#editForm').form('reset');">重置</a>
	  		<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
		</div>
	</div>
<ewcms:footer/>
<script type="text/javascript">
	$('#common').combobox({
	    mode: 'GET',
	    url: '${ctx}/yjk/zd/commonname/findbyspell?spell=',
        onChange: function (newValue, oldValue) {
        	$('#common').combobox('reload','${ctx}/yjk/zd/commonname/findbyspell?spell='+newValue);
         }
	});
	
	$(function(){
		<c:choose>
	    	<c:when test="${close}">
	    		parent.$('#edit-window').window('close');
	    	</c:when>
	    	<c:otherwise>
	    		var validationEngine = $("#editForm").validationEngine({
	    			promptPosition:'bottomRight',
	    			showOneMessage: true
	    		});
	        	<ewcms:showFieldError commandName="m"/>
	    	</c:otherwise>
		</c:choose>
	});
	$.ewcms.refresh({operate : '${operate}', data : '${lastM}'});
</script>
	