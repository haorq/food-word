package com.meiyuan.catering.merchant.dao;

import com.meiyuan.catering.merchant.entity.CateringMerchantLoginAccountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/7/1 0001 11:52
 * @Description 简单描述 : 商户登陆账号表mapper类
 * @Since version-1.0.0
 */
@Mapper
public interface CateringMerchantLoginAccountMapper extends BaseMapper<CateringMerchantLoginAccountEntity> {

    /**
     * 方法描述 : 获取当前商户账号中最大值
     * @Author: MeiTao
     * @Date: 2020/8/5 0005 9:09
     * @return: java.lang.Integer
     * @Since version-1.3.0
     */
    Integer getMaxAccountNum();

    /**
     * describe: 批量删除登陆账号
     * @author: fql
     * @date: 2020/10/22 16:42
     * @param ids
     * @return: {@link}
     * @version 1.5.0
     **/
    void batchDelEmployee(@Param("ids") List<Long> ids);
}
