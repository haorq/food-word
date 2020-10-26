package com.meiyuan.catering.es.repository;

import lombok.Data;

/**
 * @program: esdemo
 * @description: 下钻聚合分析返回对象
 * @author: X-Pacific zhang
 * @create: 2019-02-19 16:06
 **/
@Data
public class Down {
    String level1Key;
    String level2Key;
    Object value;
    @Override
    public String toString() {
        return "Down{" +
                "level_1_key='" + level1Key + '\'' +
                ", level_2_key='" + level2Key + '\'' +
                ", value=" + value +
                '}';
    }
}
