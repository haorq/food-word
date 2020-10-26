package com.meiyuan.catering.merchant.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.merchant.entity.CateringMerchantAuditEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商户审核表(CateringMerchantAudit)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 10:17:02
 */
@Mapper
public interface CateringMerchantAuditMapper extends BaseMapper<CateringMerchantAuditEntity>{


}