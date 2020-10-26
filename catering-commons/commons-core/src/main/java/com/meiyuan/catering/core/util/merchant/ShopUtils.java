package com.meiyuan.catering.core.util.merchant;

import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.util.DateTimeUtil;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * @Author MeiTao
 * @Date 2020/9/29 0029 10:06
 * @Description 简单描述 :  店铺相关工具方法
 * @Since version-1.5.0
 */
public class ShopUtils {

    /**
     * 方法描述 : 店铺当前营业状态
     *          注： 该营业状态对营业时间进行处理
     * @Author: MeiTao
     * @Date: 2020/9/29 0029 10:08
     * @param businessStatus 店铺停业状态 ： 1：营业，2：停止营业
     * @param openingTime 营业开始时间
     * @param closingTime 营业结束时间
     * @return: Integer 店铺营业状态 ： 1： 营业中、2：已打烊、3：停业中
     * @Since version-1.5.0
     */
    public static Integer getBusinessStatus(Integer businessStatus, String openingTime,String closingTime) {
        //商家是否手动关闭店铺
        if (ObjectUtils.isEmpty(businessStatus) || Objects.equals(StatusEnum.ENABLE_NOT.getStatus(),businessStatus)){
            return  3;
        }

        //当前店铺是否在营业时间内
        if (openingTime != null && closingTime != null) {
            return DateTimeUtil.getShopBusinessStatus(openingTime, closingTime);
        }

        return StatusEnum.ENABLE_NOT.getStatus();
    }
}
