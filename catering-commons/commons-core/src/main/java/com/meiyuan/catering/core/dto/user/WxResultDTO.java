package com.meiyuan.catering.core.dto.user;

import lombok.Data;

/**
 * description：
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/10 14:40
 */
@Data
public class WxResultDTO {
    // 图文消息
    public static final int NEWSMSG = 1;
    private boolean success;
    private Object obj;
    private int type;
    private String msg;
}
