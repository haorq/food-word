package com.meiyuan.catering.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.merchant.dto.auth.MerchantLoginAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantLimitQueryDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantQueryConditionDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantQueryPage;
import com.meiyuan.catering.merchant.dto.shop.ShopQueryDTO;
import com.meiyuan.catering.merchant.entity.CateringMerchantEntity;
import com.meiyuan.catering.merchant.entity.CateringShopEntity;
import com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPageVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantInfoVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantPageVo;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商户表(CateringMerchant)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 10:16:33
 */
@Mapper
public interface CateringMerchantMapper extends BaseMapper<CateringMerchantEntity>{

    /**
     * pc-商户店铺详情查询
     * @param dto
     * @return
     */
    MerchantShopDetailVo getMerchantShopDetail(@Param("dto") ShopQueryDTO dto );

    /**
     * pc-店铺列表
     * @param page
     * @param dto
     * @return
     */
    IPage<CateringMerchantPageVo> listCateringMerchantPage(Page page,@Param("dto") MerchantLimitQueryDTO dto);


    /**
     * 根据查询条件查询商户信息
     * @param queryCondition 查询条件
     * @return
     */
    List<MerchantInfoVo> listMerchantInfo(@Param("queryCondition") MerchantQueryConditionDTO queryCondition);

    /**
     * 方法描述 : 管理平台商户列表分页
     * @Author: MeiTao
     * @Date: 2020/7/3 0003 9:04
     * @param page
     * @param dto 请求参数
     * @return: IPage<com.meiyuan.catering.merchant.vo.merchant.CateringMerchantPageVo>
     * @Since version-1.2.0
     */
    IPage<MerchantPageVo> merchantPage(Page page, @Param("dto") MerchantQueryPage dto);

    /**
     * 方法描述 : 查询商户编码当前redis自增最大值
     * @Author: MeiTao
     * @Date: 2020/6/22 0022 10:53
     * @param
     * @return: java.lang.Integer
     * @Since version-1.1.1
     */
    Integer getMerchantCodeMax();

    /**
     * 方法描述 : 查询所有门店
     *          1、不包含自提点；2、包含已删除门店
     * @Author: MeiTao
     * @Date: 2020/8/26 0026 15:51
     * @return: java.util.List<CateringShopEntity>
     * @Since version-1.3.0
     */
    List<CateringShopEntity> findAll();

    /**
     * 方法描述 : 查询商户登录账号信息
     * @Author: MeiTao
     * @Date: 2020/9/2 0002 14:51
     * @param account 请求参数
     * @return: java.util.List<MerchantLoginAccountDTO>
     * @Since version-1.3.0
     */
    List<MerchantLoginAccountDTO> listMerchantAccount(@Param("account")String account);
}