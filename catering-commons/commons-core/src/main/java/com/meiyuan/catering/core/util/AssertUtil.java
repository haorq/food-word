package com.meiyuan.catering.core.util;

import com.meiyuan.catering.core.exception.CustomException;

public class AssertUtil {

    /**
     *
     * @param expression throws CustomException with provided message if false
     * @param message error message
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression){
            throw new CustomException(message);
        }
    }
}
