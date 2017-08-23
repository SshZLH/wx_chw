package com.whc.wx.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class renewTimeUtil {
	public static Map<String,String> renewTime(String retlTime){
		String endDate;
		int OverDay = 0;
		int lengthdays=0;
		String subyear = retlTime.substring(0, 4);
		String submonth = retlTime.substring(5, 7);
		String subday  = retlTime.substring(8, 10);
		int year1;
		int month1;
		int month1s;
		int day1;
		year1=Integer.valueOf(subyear);
		if(submonth.substring(0, 1).equals("0")){
			month1=Integer.valueOf(submonth.substring(1, 2));
			month1s=Integer.valueOf(submonth.substring(1, 2));
		}else{
			month1=Integer.valueOf(submonth);
			month1s=Integer.valueOf(submonth);
		}
		if(subday.substring(0, 1).equals("0")){
			day1=Integer.valueOf(subday.substring(1, 2));
		}else{
			day1=Integer.valueOf(subday);
		}
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = dateFormat.format(date);
		String stryear = dateTime.substring(0, 4);
		String strmonth = dateTime.substring(5, 7);
		String strday = dateTime.substring(8, 10);
		int year2;
		int month2;
		int day2;
		year2=Integer.valueOf(stryear);
		if(strmonth.substring(0, 1).equals("0")){
			month2=Integer.valueOf(strmonth.substring(1, 2));
		}else{
			month2=Integer.valueOf(strmonth);
		}
		if(strday.substring(0, 1).equals("0")){
			day2=Integer.valueOf(strday.substring(1, 2));
		}else{
			day2=Integer.valueOf(strday);
		}
		int renewMonth=0;
		int days=0;
		if(year1==year2){
			int day = 1;
			
			if(month1!=month2){
				for (int i = month1; i < month2+1; i++) {
					if(i<=month2){
						Calendar cal = Calendar.getInstance();
						cal.set(year1,i - 1,day);
						int last = cal.getActualMaximum(Calendar.DATE);
						if(i==month1){
							days=days+last-day1;
						}else if(i==month2){
							days=days+day2;
						}else{
							days=days+last;
						}
						lengthdays =lengthdays+last;
					}
				}
			}else{
				days=day2-day1;
				OverDay=days;
				Calendar cal = Calendar.getInstance();
				cal.set(year1,month2 - 1,day);
				lengthdays =lengthdays+cal.getActualMaximum(Calendar.DATE);
				renewMonth=1;
			}
			if(day1>day2){
				renewMonth=month2-month1s;
				Calendar cal = Calendar.getInstance();
				cal.set(year1,month2 - 2,day);
				int last = cal.getActualMaximum(Calendar.DATE);
				OverDay=last-day1+day2;
			}else{
				OverDay=day2-day1;
				renewMonth=month2-month1s+1;
			}
			System.out.println("OverDay:"+OverDay);
			System.out.println("days:"+days);
			System.out.println("renewMonth:"+renewMonth);
			
		}else{
			int day = 1;
			
			for(int n =year1;n<= year2;n++){
				if(n==year1){
					for (int i = month1; i < 12+1; i++) {
						if(i<=12){
							Calendar cal = Calendar.getInstance();
							cal.set(year1,i - 1,day);
							int last = cal.getActualMaximum(Calendar.DATE);
							if(i==month1){
								days=days+last-day1;
							}else{
								days=days+last;
							}
							lengthdays =lengthdays+last;
						}
					}
					renewMonth=renewMonth+12-month1s;
				}else if(n==year2){
					for(int i=1;i<month2+1;i++){
						if(i<=month2){
							Calendar cal = Calendar.getInstance();
							cal.set(year2,i - 1,day);
							int last = cal.getActualMaximum(Calendar.DATE);
							if(i==month2){
								days=days+day2;
							}else{
								days=days+last;
							}
							lengthdays =lengthdays+last;
						}
					}
					
					if(day1>day2){
						Calendar cal = Calendar.getInstance();
						cal.set(year1,month2 - 2,day);
						int last = cal.getActualMaximum(Calendar.DATE);
						OverDay=last-day1+day2+1;
						renewMonth=renewMonth+month2;
					}else{
						OverDay=day2-day1;
						renewMonth=renewMonth+month2+1;
					}
					System.out.println("OverDay:::::"+OverDay);
				}else{
					for(int i=1;i<12+1;i++){
						if(i<=12){
							Calendar cal = Calendar.getInstance();
							cal.set(n,i - 1,day);
							int last = cal.getActualMaximum(Calendar.DATE);
							days=days+last;
							lengthdays =lengthdays+last;
						}
					}
					renewMonth=renewMonth+12;
				}
			}
			System.out.println("days:"+days);
			System.out.println("renewMonth:"+renewMonth);
			
		}
		String tMonth=Integer.toString(month2+1);
		if(tMonth.length()==1){
			tMonth="0"+tMonth;
		}
		endDate=Integer.toString(year1)+"-"+tMonth+"-"+retlTime.substring(8, 19);
		Map<String,String> map=new HashMap<String, String>();
		map.put("renewMonth",Integer.toString(renewMonth));
		map.put("OverDay", Integer.toString(OverDay));
		map.put("endDate", endDate);
		 Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		 int dayn=aCalendar.getActualMaximum(Calendar.DATE);
		map.put("lengthdays", String.valueOf(lengthdays-dayn));
		return map;
	}
	//结束时间计算
	public static Map<String,String> renewSelectTime(String unit,String endDate,String selectTime){
		String selectEndDate = null;
		if(unit.equals("1")){
			selectEndDate=timeConversion(Integer.valueOf(selectTime),endDate);
		}else if(unit.equals("2")){
			selectEndDate=timeConversion(Integer.valueOf(selectTime)*24,endDate);
		}else if(unit.equals("3")){
			SimpleDateFormat sf=new SimpleDateFormat("dd");
			Calendar cal = Calendar.getInstance();
			int month=cal.get(Calendar.MONTH);
			System.out.println(month);
			int days=0;
			for (int i = 0; i <= Integer.valueOf(selectTime); i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.MONTH, month+i+1);
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				String thisMaxDay = sf.format(calendar.getTime());
				System.out.println(thisMaxDay);
				if(i==0){
					days =days+ Integer.valueOf(thisMaxDay)-Integer.valueOf(endDate.substring(8, 10));
				}else if(i==Integer.valueOf(selectTime)){
					days =days+Integer.valueOf(endDate.substring(8, 10));
				}else{
					days =days+Integer.valueOf(thisMaxDay);
				}
				
			}
			selectEndDate=timeConversion(days*24,endDate);
		}
		Map<String,String> map=new HashMap<String, String>();
		map.put("selectEndDate", selectEndDate);
		return map;
	}
	public static String timeConversion(int length,String endDate){
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			Date date = dateFormat.parse(endDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			long i = calendar.getTimeInMillis();
			long n = 3600000*(long)length;
			Date date1=new Date(i+n);
			endDate=dateFormat.format(date1);
			System.out.println(endDate);
		} catch (ParseException e){
			e.printStackTrace();
		}
		
		return endDate;
	}
}
