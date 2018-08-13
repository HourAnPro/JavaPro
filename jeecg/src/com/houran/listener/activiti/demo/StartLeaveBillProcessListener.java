package com.houran.listener.activiti.demo;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

public class StartLeaveBillProcessListener implements TaskListener,ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask dt) {
		System.out.println("StartLeaveBillProcessListener执行了！！！");
		
	}

	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("StartLeaveBillProcessListener执行了！！！");
	}

}
