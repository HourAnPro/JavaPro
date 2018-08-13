package com.houran.controller.activiti.demo;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.TreeChildCount;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import org.jeecgframework.core.util.ExceptionUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.jwt.util.GsonUtil;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import com.alibaba.fastjson.JSONArray;
import com.houran.entity.activiti.Leave;
import com.houran.entity.activiti.demo.OaLeavebillFormEntity;
import com.houran.service.activiti.demo.OaLeavebillFormServiceI;
import com.houran.util.activiti.HistoryProcessInstanceDiagramCmd;
import com.houran.util.activiti.Variable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

/**   
 * @Title: Controller  
 * @Description: 工作流请假单
 * @author onlineGenerator
 * @date 2018-07-27 15:35:43
 * @version V1.0   
 *
 */
@Api(value="OaLeavebillForm",description="工作流请假单",tags="oaLeavebillFormController")
@Controller
@RequestMapping("/oaLeavebillFormController")
public class OaLeavebillFormController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OaLeavebillFormController.class);

	@Autowired
	private OaLeavebillFormServiceI oaLeavebillFormService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
    protected TaskService taskService;
	@Autowired
	private HistoryService historyService;
	
	//doLeaveBillSummit
	/**
	 * 提交工作流请假单申请
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doLeaveBillSummit")
	@ResponseBody
	public AjaxJson doLeaveBillSummit(OaLeavebillFormEntity oaLeavebillForm, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "提交请假单申请成功";
		try{
			oaLeavebillForm=oaLeavebillFormService.getEntity(OaLeavebillFormEntity.class, oaLeavebillForm.getId());
			oaLeavebillForm.setUserId(ResourceUtil.getSessionUser().getId());
			oaLeavebillForm.setState(1);
			oaLeavebillFormService.leaveBillWorkFlowStart(oaLeavebillForm);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "提交请假单申请失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * easyui 流程历史数据获取
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "taskHistoryList")
	public void taskHistoryList(@RequestParam("processInstanceId") String processInstanceId,
			HttpServletRequest request, HttpServletResponse response,DataGrid dataGrid) {
		
        List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).list();
        
        StringBuffer rows = new StringBuffer();
        for(HistoricTaskInstance hi : historicTasks){
			rows.append("{'name':'"+hi.getName()+"','processInstanceId':'"+hi.getProcessInstanceId() +"','startTime':'"+hi.getStartTime()+"','endTime':'"+hi.getEndTime()+"','assignee':'"+hi.getAssignee()+"','deleteReason':'"+hi.getDeleteReason()+"'},");
        	//System.out.println(hi.getName()+"@"+hi.getAssignee()+"@"+hi.getStartTime()+"@"+hi.getEndTime());
        }
		
		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		
		JSONObject jObject = JSONObject.fromObject("{'total':"+historicTasks.size()+",'rows':["+rowStr+"]}");
		responseDatagrid(response, jObject);
	}
	
	/**
	 * 读取带跟踪的流程图片
	 * @throws Exception
	 */
	@RequestMapping(params = "traceImage")
    public void traceImage(@RequestParam("processInstanceId") String processInstanceId,
    		HttpServletResponse response) throws Exception {
    	
		Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(
                processInstanceId);

		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(); 
        InputStream is = processEngine.getManagementService().executeCommand(
                cmd);
        
        int len = 0;
        byte[] b = new byte[1024];

        while ((len = is.read(b, 0, 1024)) != -1) {
        	response.getOutputStream().write(b, 0, len);
        }
    }
	
	/**
	 * easyui 流程历史页面
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "viewProcessInstanceHistory")
	public ModelAndView viewProcessInstanceHistory(@RequestParam("processInstanceId") String processInstanceId,
			HttpServletRequest request, HttpServletResponse respone,Model model) {
		
		model.addAttribute("processInstanceId", processInstanceId);
		
		return new ModelAndView("com/houran/activiti/demo/viewProcessInstanceHistory");
	}


	/**
	 * 工作流请假单列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/houran/activiti/demo/oaLeavebillFormList");
	}
	
	
	/**
	 * easyui 已办任务页面
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "finishedTask")
	public ModelAndView finishedTask() {
		
		return new ModelAndView("com/houran/activiti/demo/finishedTask");
	}
	
	/**
	 * easyui AJAX请求数据 已办任务
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "finishedTaskDataGrid")
	public void finishedTask(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		
		String userId=ResourceUtil.getSessionUser().getUserName();
		List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery().taskAssignee(userId)
                .finished().list();
		
		StringBuffer rows = new StringBuffer();
		for(HistoricTaskInstance t : historicTasks){
			rows.append("{'name':'"+t.getName() +"','description':'"+t.getDescription()+"','taskId':'"+t.getId()+"','processDefinitionId':'"+t.getProcessDefinitionId()+"','processInstanceId':'"+t.getProcessInstanceId()+"'},");
		}
		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		
		JSONObject jObject = JSONObject.fromObject("{'total':"+historicTasks.size()+",'rows':["+rowStr+"]}");
		responseDatagrid(response, jObject);
	}
	
	
	/**
     * 完成任务
     * @param deploymentId 完成任务
     */
	@RequestMapping(params = "completeTask")
	@ResponseBody
	public AjaxJson completeTask(String id,String taskId,Variable var,HttpServletRequest request) {
		AjaxJson j = new AjaxJson();				
		
		Map<String, Object> variables = var.getVariableMap();
		
		//Map<String, Object> map=new HashMap<String, Object>();
		//variables.put("applyUserId", "审批人");
		//variables.put("reportBackEndProcessor", "同意");
        taskService.complete(taskId, variables);
		
		//请假流程启动
		//leaveService.leaveWorkFlowStart(leave);
        
        ProcessInstance processInstance=runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(id).singleResult();
        if(processInstance==null){
        	OaLeavebillFormEntity oaLeavebillFormEntity=oaLeavebillFormService.getEntity(OaLeavebillFormEntity.class, id);
        	oaLeavebillFormEntity.setState(2);
        	try {
				oaLeavebillFormService.saveOrUpdate(oaLeavebillFormEntity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        if(StringUtil.isNotEmpty(variables)){
        	if(StringUtil.isNotEmpty(var.getValues())&&var.getValues().equals("false")){
        		OaLeavebillFormEntity oaLeavebillFormEntity=oaLeavebillFormService.getEntity(OaLeavebillFormEntity.class, id);
            	oaLeavebillFormEntity.setState(3);
            	try {
    				oaLeavebillFormService.saveOrUpdate(oaLeavebillFormEntity);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        }
        
        
		String message = "办理成功";
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 完成任务表单选择
	 */
	@RequestMapping(params = "taskCompletePageSelect")
	public ModelAndView taskCompletePageSelect(@RequestParam("jspPage") String jspPage,
			@RequestParam("processInstanceId") String processInstanceId,
			@RequestParam("taskId") String taskId,HttpServletRequest request,Model model) {
			
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
			
			String businessKey = processInstance.getBusinessKey();

			OaLeavebillFormEntity leave = oaLeavebillFormService.getEntity(OaLeavebillFormEntity.class, businessKey);
		
			model.addAttribute("processInstanceId", processInstanceId);
			model.addAttribute("taskId", taskId);
			model.addAttribute("leave",leave);
			
			System.out.println(jspPage);
		
			return new ModelAndView("com/houran/activiti/demo/"+jspPage.substring(0, jspPage.lastIndexOf(".")));
	}
	
	
	/**
	 * easyui 待办任务页面
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "claimedTask")
	public ModelAndView claimedTask() {
		
		return new ModelAndView("com/houran/activiti/demo/claimedTask");
	}
	
	
	
	/**
	 * easyui AJAX请求数据 待办任务
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "claimedTaskDataGrid")
	public void claimedTaskDataGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		
		String userId=ResourceUtil.getSessionUser().getUserName();
		TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).list();
		
		StringBuffer rows = new StringBuffer();
		for(Task t : tasks){
			rows.append("{'name':'"+t.getName() +"','description':'"+t.getDescription()+"','taskId':'"+t.getId()+"','processDefinitionId':'"+t.getProcessDefinitionId()+"','processInstanceId':'"+t.getProcessInstanceId()+"'},");
		}
		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		
		JSONObject jObject = JSONObject.fromObject("{'total':"+tasks.size()+",'rows':["+rowStr+"]}");
		responseDatagrid(response, jObject);
	}
	
	
	
	/**
     * 签收任务
     * @param taskId
     */
	@RequestMapping(params = "claimTask")
	@ResponseBody
	public AjaxJson claimTask(@RequestParam("taskId") String taskId, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		
		String userId=ResourceUtil.getSessionUser().getUserName();
		
		TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskId, userId);
		
		String message = "签收成功";
		j.setMsg(message);
		return j;
	}
	
	/**
	 * easyui 待领任务页面
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "waitingClaimTask")
	public ModelAndView waitingClaimTask() {
		
		return new ModelAndView("com/houran/activiti/demo/waitingClaimTask");
	}
	
	
	/**
	 * easyui AJAX请求数据 待领任务
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "waitingClaimTaskDataGrid")
	public void waitingClaimTaskDataGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String userId=ResourceUtil.getSessionUser().getUserName();
//		ProcessInstance processInstance=processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceBusinessKey("").singleResult();
		TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser(userId).active().list();//.taskCandidateGroup("hr").active().list();
		
		StringBuffer rows = new StringBuffer();
		for(Task t : tasks){
			rows.append("{'name':'"+t.getName() +"','taskId':'"+t.getId()+"','processDefinitionId':'"+t.getProcessDefinitionId()+"'},");
		}
		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		
		JSONObject jObject = JSONObject.fromObject("{'total':"+tasks.size()+",'rows':["+rowStr+"]}");
		responseDatagrid(response, jObject);
		
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(OaLeavebillFormEntity oaLeavebillForm,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(OaLeavebillFormEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, oaLeavebillForm, request.getParameterMap());
		try{
		//自定义追加查询条件
			//cq.eq("state", 0);
			//cq.or(Restrictions.eq("state", 0), Restrictions.eq("state", 3));
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.oaLeavebillFormService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除工作流请假单
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(OaLeavebillFormEntity oaLeavebillForm, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		oaLeavebillForm = systemService.getEntity(OaLeavebillFormEntity.class, oaLeavebillForm.getId());
		message = "工作流请假单删除成功";
		try{
			oaLeavebillFormService.delete(oaLeavebillForm);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "工作流请假单删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除工作流请假单
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作流请假单删除成功";
		try{
			for(String id:ids.split(",")){
				OaLeavebillFormEntity oaLeavebillForm = systemService.getEntity(OaLeavebillFormEntity.class, 
				id
				);
				oaLeavebillFormService.delete(oaLeavebillForm);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "工作流请假单删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加工作流请假单
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(OaLeavebillFormEntity oaLeavebillForm, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作流请假单添加成功";
		try{
			oaLeavebillForm.setState(0);//初始录入
			oaLeavebillFormService.save(oaLeavebillForm);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "工作流请假单添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新工作流请假单
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(OaLeavebillFormEntity oaLeavebillForm, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作流请假单更新成功";
		OaLeavebillFormEntity t = oaLeavebillFormService.get(OaLeavebillFormEntity.class, oaLeavebillForm.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(oaLeavebillForm, t);
			oaLeavebillFormService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "工作流请假单更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 工作流请假单新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(OaLeavebillFormEntity oaLeavebillForm, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(oaLeavebillForm.getId())) {
			oaLeavebillForm = oaLeavebillFormService.getEntity(OaLeavebillFormEntity.class, oaLeavebillForm.getId());
			req.setAttribute("oaLeavebillFormPage", oaLeavebillForm);
		}
		return new ModelAndView("com/houran/activiti/demo/oaLeavebillForm-add");
	}
	/**
	 * 工作流请假单编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(OaLeavebillFormEntity oaLeavebillForm, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(oaLeavebillForm.getId())) {
			oaLeavebillForm = oaLeavebillFormService.getEntity(OaLeavebillFormEntity.class, oaLeavebillForm.getId());
			req.setAttribute("oaLeavebillFormPage", oaLeavebillForm);
		}
		return new ModelAndView("com/houran/activiti/demo/oaLeavebillForm-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","oaLeavebillFormController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(OaLeavebillFormEntity oaLeavebillForm,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(OaLeavebillFormEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, oaLeavebillForm, request.getParameterMap());
		List<OaLeavebillFormEntity> oaLeavebillForms = this.oaLeavebillFormService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"工作流请假单");
		modelMap.put(NormalExcelConstants.CLASS,OaLeavebillFormEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("工作流请假单列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,oaLeavebillForms);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(OaLeavebillFormEntity oaLeavebillForm,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"工作流请假单");
    	modelMap.put(NormalExcelConstants.CLASS,OaLeavebillFormEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("工作流请假单列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
    	"导出信息"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<OaLeavebillFormEntity> listOaLeavebillFormEntitys = ExcelImportUtil.importExcel(file.getInputStream(),OaLeavebillFormEntity.class,params);
				for (OaLeavebillFormEntity oaLeavebillForm : listOaLeavebillFormEntitys) {
					oaLeavebillFormService.save(oaLeavebillForm);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}finally{
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="工作流请假单列表信息",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<OaLeavebillFormEntity>> list() {
		List<OaLeavebillFormEntity> listOaLeavebillForms=oaLeavebillFormService.getList(OaLeavebillFormEntity.class);
		return Result.success(listOaLeavebillForms);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="根据ID获取工作流请假单信息",notes="根据ID获取工作流请假单信息",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		OaLeavebillFormEntity task = oaLeavebillFormService.get(OaLeavebillFormEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取工作流请假单信息为空");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="创建工作流请假单")
	public ResponseMessage<?> create(@ApiParam(name="工作流请假单对象")@RequestBody OaLeavebillFormEntity oaLeavebillForm, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<OaLeavebillFormEntity>> failures = validator.validate(oaLeavebillForm);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			oaLeavebillFormService.save(oaLeavebillForm);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("工作流请假单信息保存失败");
		}
		return Result.success(oaLeavebillForm);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="更新工作流请假单",notes="更新工作流请假单")
	public ResponseMessage<?> update(@ApiParam(name="工作流请假单对象")@RequestBody OaLeavebillFormEntity oaLeavebillForm) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<OaLeavebillFormEntity>> failures = validator.validate(oaLeavebillForm);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			oaLeavebillFormService.saveOrUpdate(oaLeavebillForm);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("更新工作流请假单信息失败");
		}

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success("更新工作流请假单信息成功");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="删除工作流请假单")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			oaLeavebillFormService.deleteEntityById(OaLeavebillFormEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("工作流请假单删除失败");
		}

		return Result.success();
	}
	
	// -----------------------------------------------------------------------------------
			// 以下各函数可以提成共用部件 (Add by Quainty)
			// -----------------------------------------------------------------------------------
			public void responseDatagrid(HttpServletResponse response, JSONObject jObject) {
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-store");
				try {
					PrintWriter pw=response.getWriter();
					pw.write(jObject.toString());
					pw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	
	
}
