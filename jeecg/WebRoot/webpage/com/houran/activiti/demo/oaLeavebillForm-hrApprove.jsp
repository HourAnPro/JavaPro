<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>工作流请假单</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="oaLeavebillFormController.do?completeTask" >
					<input id="id" name="id" type="hidden" value="${leave.id }"/>
					<input id="processInstanceId" name="processInstanceId" type="hidden" value="${processInstanceId}"/>
					<input id="taskId" name="taskId" type="hidden" value="${taskId}"/>
					<input id="keys" name="keys" type="hidden" value="hrPass"/>
					<input id="types" name="types" type="hidden" value="B">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<%-- <tr>
						<td align="right">
							<label class="Validform_label">
								用户id:
							</label>
						</td>
						<td class="value">
						    <input id="userId" name="userId" type="text" maxlength="255" style="width: 150px" class="inputxt"  ignore="ignore"  value='${oaLeavebillFormPage.userId}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">用户id</label>
						</td>
					</tr> --%>
					<tr>
						<td align="right">
							<label class="Validform_label">
								流程发起人:
							</label>
						</td>
						<td class="value">
						    <input id="reason" name="reason" type="text" maxlength="500" style="width: 150px" class="inputxt"  ignore="ignore"  value='${leave.userId}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">流程发起人</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								请假原因:
							</label>
						</td>
						<td class="value">
						    <input id="reason" name="reason" type="text" maxlength="500" style="width: 150px" class="inputxt"  ignore="ignore"  value='${leave.reason}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">请假原因</label>
						</td>
					</tr>
					
					<tr>
						<td align="right">
							<label class="Validform_label">
								是否同意:
							</label>
						</td>
						<td class="value">
						   	<select name="values">
						   		<option  value="true">同意</option>
						   		<option value="false">不同意</option>						 
						   	</select>						   	
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">请假原因</label>
						</td>
					</tr>
					<%-- <tr>
						<td align="right">
							<label class="Validform_label">
								状态:
							</label>
						</td>
						<td class="value">
						    <input id="state" name="state" type="text" maxlength="10" style="width: 150px" class="inputxt"  datatype="n"  ignore="ignore"  value='${oaLeavebillFormPage.state}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">状态</label>
						</td>
					</tr> --%>
					<%-- <tr>
						<td align="right">
							<label class="Validform_label">
								流程实例id:
							</label>
						</td>
						<td class="value">
						    <input id="processInstanceid" name="processInstanceid" type="text" maxlength="255" style="width: 150px" class="inputxt"  ignore="ignore"  value='${oaLeavebillFormPage.processInstanceid}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">流程实例id</label>
						</td>
					</tr> --%>
				
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/com/houran/demo/oaLeavebillForm.js"></script>		
