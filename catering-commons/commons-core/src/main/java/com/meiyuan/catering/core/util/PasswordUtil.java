package com.meiyuan.catering.core.util;

import com.meiyuan.catering.core.constant.DefaultPwdMsg;

import java.util.Random;

/**
 * @Author MeiTao
 * @Description 密码生成工具类
 * @Date  2020/4/15 0016 15:26
 */
public class PasswordUtil {
	private static Random random = new Random();

	/**
	 * 生成一批密码
	 * <br>生成规则：
	 * 大写字母+小写字母+数字:密码长度6-10
	 * @return
	 */
	public static String getPassword(){
        //密码长度6位
		int total= 6;

        StringBuffer password = new StringBuffer();

        for (int i= 0; i< total;i++){
			password.append(getLetter(3));
        }

//        设置默认密码为123456
		String strPassword = password.toString();
		if (DefaultPwdMsg.USE_DEFAULT_PWD){
			strPassword = DefaultPwdMsg.DEFAULT_PWD;
		}
		return strPassword;
	}



	/**
	 * 随机获取一个小写字母
	 */
	public static char getLetter(){
		char c=(char) getRandomInt(97, 122);
		return c;
	}
	
	/**
	 * 随机获取一个大写字母
	 */
	public static char getUpper(){
        random.nextInt();
		char c=(char) getRandomInt(65, 90);
		return c;
	}
	
	/**
	 * 随机获取一个 min-max 的区间内的数字
	 * @return
	 */
	private static int getRandomInt(int min, int max){
        return random.nextInt(max-min+1)+min;
	}

    /**
     * 获取一个大写字母，小写字母，或者数字
     * @param type:1:大写字母，2:小写字母，3:数字
     * @return
     */
	private static String getLetter(int type){
	    String  s;
	    switch (type) {
            case 1:
                s = String.valueOf(getUpper());
                break;
            case 2:
                s = String.valueOf(getLetter());
                break;
            default:
                s = String.valueOf(getRandomInt(0, 9));
                break;
        }
		return s;
	}

}
