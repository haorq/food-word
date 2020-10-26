package com.meiyuan.catering.core.enums.base.merchant.dada;

import lombok.Getter;

/**
 * @Author MeiTao
 * @Descriptio 哒哒添加门店信息哒哒返回错误码即提示信息
 * @Date  2020/3/10 0010 13:39
 */
@Getter
public enum DadaErrEnum {
    /**
     * 请求成功
     **/
    SUCCESS(Values.SUCCESS, "请求成功"),
    /**
     * 未知异常
     **/
    ERR_UNKNOWN(Values.ERR_UNKNOWN, "达达配送开启失败，请联系平台管理人员"),
    /**
     * 城市尚未开通达达配送
     **/
    ERR_CITY_1(Values.ERR_CITY_1, "当前城市不支持达达配送"),
    /**
     * 城市区号不能为空
     */
    ERR_CITY_2(Values.ERR_CITY_2, "当前城市不支持达达配送"),
    /**
     * 城市名称city_name不正确
     */
    ERR_CITY_3(Values.ERR_CITY_3, "当前城市不支持达达配送"),
    /**
     * 区域名称area_name不正确
     */
    ERR_CITY_4(Values.ERR_CITY_4, "当前城市不支持达达配送"),
    /**
     * 新的门店编号不能与现有的门店编号相同
     **/
    ERR_CITY_5(Values.ERR_CITY_5, "当前城市不支持达达配送"),
    /**
     * cityCode不能为空
     **/
    ERR_CITY_6(Values.ERR_CITY_6, "当前城市不支持达达配送"),
    /**
     * 门店联系人电话不能为空
     **/
    ERR_CITY_7(Values.ERR_CITY_7, "当前城市不支持达达配送"),
    /**
     * 门店联系人电话号码无效或格式不正确
     **/
    ERR_PHONE_8(Values.ERR_PHONE_8, "门店联系人电话号码无效"),
    /**
     * 门店联系人电话号码无效或格式不正确
     **/
    ERR_PHONE_9(Values.ERR_PHONE_9, "门店联系人电话号码无效"),
    /**
     * 手机号不正确
     **/
    ERR_PHONE_10(Values.ERR_PHONE_10, "门店联系人电话号码无效"),
    /**
     * 手机号不正确
     **/
    ERR_CITY_11(Values.ERR_CITY_11, "当前城市不支持达达配送"),

    ERR_CITY_12(Values.ERR_CITY_12, "当前城市不支持达达配送");

    private Integer status;
    private String desc;
    DadaErrEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static DadaErrEnum parse(int status) {
        for (DadaErrEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        return DadaErrEnum.parse(200);
    }

    public static class  Values{
        private static final Integer SUCCESS = 0;
        private static final Integer ERR_UNKNOWN = 200;
        private static final Integer ERR_CITY_1 = 2044;
        private static final Integer ERR_CITY_2 = 2056;
        private static final Integer ERR_CITY_3 = 2404;
        private static final Integer ERR_CITY_4 = 2405;
        private static final Integer ERR_CITY_5 = 2409;
        private static final Integer ERR_CITY_6 = 2532;
        private static final Integer ERR_CITY_7 = 7703;
        private static final Integer ERR_PHONE_8= 8812;
        private static final Integer ERR_PHONE_9= 9918;
        private static final Integer ERR_PHONE_10= 9924;
        private static final Integer ERR_CITY_11 = 7718;
        private static final Integer ERR_CITY_12 = 2432;

    }
}
