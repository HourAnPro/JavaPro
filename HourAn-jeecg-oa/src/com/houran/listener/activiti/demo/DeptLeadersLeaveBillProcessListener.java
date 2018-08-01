package com.houran.listener.activiti.demo;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.jeecgframework.core.util.ResourceUtil;

public class DeptLeadersLeaveBillProcessListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask dt) {
		dt.addCandidateUser(ResourceUtil.getSessionUser().getUserName());//分配个人任务
		
	}

}
