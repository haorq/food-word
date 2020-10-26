package com.meiyuan.catering.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * @author admin
 */
public class CharUtil {

	public static String getRandomString(Integer num) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String getRandomNum(Integer num) {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String disposeChar(String before){
		if(StringUtils.isNotBlank(before)){
			before = before.replaceAll("\\\\", "\\\\\\\\");
			before = before.replaceAll("_", "\\\\_");
			before = before.replaceAll("%", "\\\\%");
		}
		return before ;
	}
}
