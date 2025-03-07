package com.meiyuan.catering.admin.log.enums;

/**
 * @Author yz
 * @Date 2020/6/23 0023 17:46
 * @Description 简单描述 :  操作状态枚举
 * @Since version-1.0.0
 */
public enum OperationStatusEnum {
    /**
     * 失败
     */
    FAIL(0),
    /**
     * 成功
     */
    SUCCESS(1);

    private int value;

    OperationStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
