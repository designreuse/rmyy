<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<ewcms:head title="患者信息所含诊疗卡"/>
	<table id="tt" class="easyui-datagrid" data-options="toolbar:'#tb',fit:true,url:'${ctx}/empi/card/manage/practicecard/${patientBaseInfoId}/query',nowrap:true,pagination:true,rownumbers:true,striped:true,pageSize:10">
		<thead>
			<tr>
			    <th data-options="field:'practiceNo',width:100">诊疗卡号</th>
			     <th data-options="field:'createDate',width:150">上传时间</th>
		</thead>
	</table>
	<div id="tb" style="padding:5px;height:auto;">
		<div class="toolbar" style="margin-bottom:2px">
			
		</div>
		<div style="padding-left:5px;">
        	<form id="queryform" style="padding:0;margin:0;">
        		<table class="formtable">
              		      			
           		</table>
        	</form>
        </div>
	</div>
<ewcms:editWindow/>
<ewcms:footer/>
<script type="text/javascript">
	

</script>