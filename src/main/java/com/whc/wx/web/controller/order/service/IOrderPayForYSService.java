package com.whc.wx.web.controller.order.service;
/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/6/28
 * Time：17:51
 */
public interface IOrderPayForYSService{
	public String  updateOrderStatus(String orderId, String transactionId);
	public String  updateOrderStatusByCoverWarehouse(String coverWarehouseAmount, String endDate, String orderId, String transactionId);
}
