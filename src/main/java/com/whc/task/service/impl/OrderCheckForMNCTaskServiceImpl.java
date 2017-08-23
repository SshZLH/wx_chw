package com.whc.task.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.wanhuchina.common.util.springcontext.SpringContextUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.whc.task.service.IOrderForMNCService;
import com.whc.task.service.IOrderCheckForMNCTaskService;

import javax.annotation.Resource;

@Service("com.whc.task.service.IOrderCheckForMNCTaskService")
public class OrderCheckForMNCTaskServiceImpl implements IOrderCheckForMNCTaskService {

	@Resource
	private IOrderForMNCService iOrderForMNCService;

	@Scheduled(cron="0 45 10 * * ?")
	@Override
	public void execute() {
		Thread _thread=new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("sj:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"ִ开始:");
				//getOrderService().checkOrderStatus();
				iOrderForMNCService.checkOrderStatus();
				System.out.println("sj:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"ִ结束:");

			}
		});
		_thread.run();
	}
//	public IOrderForMNCService getOrderService() {
//		System.out.println("111111");
//		return (OderForMNCServiceImpl)SpringContextUtil.getBean("OderForMNCServiceImpl");
//	}
}
