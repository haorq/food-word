package com.meiyuan.catering.admin.enums.base;

/**
 * @author lhm
 * @date 2020/3/21 14:02
 **/
public enum  AdminResponseEnum {
    /**
     * 用户帐号或密码不正确
     */
    ADMIN_INVALID_ACCOUNT_OR_PASSWORD("用户帐号或密码不正确"),
    /**
     * 认证失败
     */
    ADMIN_INVALID_AUTH("认证失败"),
    /**
     * 认证失败
     */
    ADMIN_NAME_NOT_EXIST("认证失败"),
    /**
     * 原始密码错误，请重新输入
     */
    ADMIN_INVALID_OLD_PASSWORD( "原始密码错误，请重新输入"),
    /**
     * 密码错误，请重新输入
     */
    ADMIN_INVALID_PASSWORD( "密码错误，请重新输入"),
    /**
     * 用户帐号已禁用
     */
    ADMIN_LOCK_ACCOUNT("用户帐号已禁用");

    private final String desc;

    AdminResponseEnum(String desc) {
        this.desc = desc;
    }
    public String desc() {
        return desc;
    }}
