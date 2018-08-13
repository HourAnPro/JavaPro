<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="oaLeavebillFormList" checkbox="false" pagination="true" fitColumns="true" title="工作流请假单" actionUrl="oaLeavebillFormController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <%-- <t:dgCol title="流程状态"  field="bpmStatus"  queryMode="single"  dictionary="bpm_status"  width="120"></t:dgCol> --%>
   <%-- <t:dgCol title="用户id"  field="userId"  queryMode="single"  width="120"></t:dgCol> --%>
   <t:dgCol title="请假原因"  field="reason"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="状态"  field="state" dictionary="LeaveSate"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程实例id"  field="processInstanceid" hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="oaLeavebillFormController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   
   <t:dgFunOpt  funname="doLeaveBillSummit(id)" title="提交申请"   exp="state#eq#0"  urlclass="ace_button" urlfont="fa-wrench"/>
   <%-- <t:dgFunOpt  funname="doLeaveBillSummit(id)" title="提交申请"   exp="state#eq#3"  urlclass="ace_button" urlfont="fa-wrench"/> --%>
   <t:dgFunOpt  funname="viewAuditPro(processInstanceid)" title="查看审核进度"     urlclass="ace_button" urlfont="fa-wrench"/>
   
   <t:dgToolBar title="录入" icon="icon-add" url="oaLeavebillFormController.do?goAdd" funname="add"></t:dgToolBar>
	<t:dgToolBar title="编辑" icon="icon-edit" url="oaLeavebillFormController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="oaLeavebillFormController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="oaLeavebillFormController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/com/houran/demo/oaLeavebillFormList.js"></script>	
 
 <script type="text/javascript">
 function viewAuditPro(processInstanceid){
	 var viewUrl="oaLeavebillFormController.do?viewProcessInstanceHistory&processInstanceId="+processInstanceid+"&isIframe";
	 createdetailwindow("查看请假审核进度",viewUrl,"100%","100%");//只带关闭铵钮
 }
 
	//自定义按钮-提交请假单，启动流程实例
	function doLeaveBillSummit(id){	
	    var url = "oaLeavebillFormController.do?doLeaveBillSummit";
		url = url+"&id="+id;
		createdialog('确认 ', '确定提交申请吗？', url,'oaLeavebillFormList');
	}
</script>
 	
 <script type="text/javascript">
 $(document).ready(function(){
 });
 
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'oaLeavebillFormController.do?upload', "oaLeavebillFormList");
}

//导出
function ExportXls() {
	JeecgExcelExport("oaLeavebillFormController.do?exportXls","oaLeavebillFormList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("oaLeavebillFormController.do?exportXlsByT","oaLeavebillFormList");
}

 </script>