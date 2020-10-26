package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * created on 2020/8/18 14:07
 *
 * @author yaozou
 * @since v1.0.0
 */
public enum TradeCodeEnums {
    /**  电商业务：代收消费金[3001] + 代付购买金[4001] */
    E_COMMERCE_AGENT_COLLECT("3001","电商及其它","代收消费金"),
    E_COMMERCE_AGENT_CPAY("4001","电商及其它","代付购买金"),

    /** 返利/返佣业务：代收（佣金/返利）金[3002] + 代付（佣金/返利）金[4002] */
    BROKERAGE_AGENT_COLLECT("3002","电商及其它","代收（佣金/返利）金"),
    BROKERAGE_AGENT_CPAY("4002","电商及其它","代付（佣金/返利）金"),
    ;
    private String code;
    private String name;
    private String scenario;
    TradeCodeEnums(String code,String scenario,String name){
        this.code = code;
        this.scenario = scenario;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getScenario() {
        return scenario;
    }
}
