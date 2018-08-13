package com.houran.service.activiti.demo;
import org.jeecgframework.core.common.service.CommonService;

import com.houran.entity.activiti.Leave;
import com.houran.entity.activiti.demo.OaLeavebillFormEntity;

import java.io.Serializable;

public interface OaLeavebillFormServiceI extends CommonService{
	
 	public void delete(OaLeavebillFormEntity entity) throws Exception;
 	
 	public Serializable save(OaLeavebillFormEntity entity) throws Exception;
 	
 	public void saveOrUpdate(OaLeavebillFormEntity entity) throws Exception;
 	
 	public void leaveBillWorkFlowStart(OaLeavebillFormEntity entity);
 	
}
