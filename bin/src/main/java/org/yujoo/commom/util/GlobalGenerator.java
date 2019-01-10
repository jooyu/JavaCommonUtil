package com.fenqile.leka.group.util;

import java.util.Date;
import java.util.Random;

/**
 * 编号生成器
 * @author weslywang
 *
 */
public class GlobalGenerator {
	
	//private static final String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	private static final String str="123456789";
	
	
	/**
	 * 生成活动编号
	 * @return
	 */
	public static String activityId() {
		StringBuilder sb = new StringBuilder();
		sb.append("AC").append(DateUtils.date2str(new Date())).append(generatorRandomCode(4));
		return sb.toString();
	}
	
	/**
	 * 生成团id
	 * @param suffix
	 * @return
	 */
	public static String groupId(String suffix) {
		StringBuilder sb = new StringBuilder();
		sb.append("G").append(DateUtils.date2str(new Date())).append(generatorRandomCode(4)).append(suffix);
		return sb.toString();
	}
	
	private static String generatorRandomCode(int length) {
		StringBuilder sb=new StringBuilder(length);
		for(int i=0;i<4;i++) {
		   char ch=str.charAt(new Random().nextInt(str.length()));
		   sb.append(ch);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(groupId("12345"));
	}
}
