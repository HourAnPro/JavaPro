package com.houran.listener.activiti.demo;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.jeecgframework.core.util.ResourceUtil;

public class XiaoJiaLeaveBillProcessListener implements TaskListener {

	@Override
	public void notify(DelegateTask dt) {
		dt.addCandidateUser(ResourceUtil.getSessionUser().getUserName());//分配个人任务
		
	}

}
