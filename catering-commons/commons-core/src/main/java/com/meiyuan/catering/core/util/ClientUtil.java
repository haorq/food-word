package com.meiyuan.catering.core.util;

import com.meiyuan.catering.core.exception.CustomException;

/**
 * @author zengzhangni
 * @date 2020/5/27 16:08
 * @since v1.1.0
 */
public class ClientUtil {

    /**
     * 描述: 获取结果数据(判断 result 状态)
     *
     * @param result
     * @return T
     * @author zengzhangni
     * @date 2020/5/27 16:10
     * @since v1.1.0
     */
    public static <T> T getDate(Result<T> result) {
        isFailThrowException(result);
        return result.getData();
    }


    /**
     * 描述:判断 result 是否失败
     *
     * @param result
     * @return boolean
     * @author zengzhangni
     * @date 2020/5/27 16:10
     * @since v1.1.0
     */
    public static boolean isFail(Result result) {
        return result.getCode() != 0;
    }

    /**
     * 描述:如果调用失败 抛出异常
     *
     * @param result
     * @return void
     * @author zengzhangni
     * @date 2020/5/27 16:11
     * @since v1.1.0
     */
    public static void isFailThrowException(Result result) {
        if (isFail(result)) {
            throw new CustomException(result.getCode(), result.getMsg());
        }
    }
}
