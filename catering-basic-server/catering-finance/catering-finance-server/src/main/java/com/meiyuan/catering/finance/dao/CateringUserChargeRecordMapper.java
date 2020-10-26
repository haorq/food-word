package com.meiyuan.catering.finance.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.finance.entity.CateringUserChargeRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.finance.query.recharge.RechargeRecordQueryDTO;
import com.meiyuan.catering.finance.vo.recharge.RechargeRecordListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
@Mapper
public interface CateringUserChargeRecordMapper extends BaseMapper<CateringUserChargeRecordEntity> {

    /**
     * 充值列表
     *
     * @param page
     * @param dto
     * @param userIds
     * @return
     */
    IPage<RechargeRecordListVO> pageList(Page page, @Param("dto") RechargeRecordQueryDTO dto, @Param("userIds") List<Long> userIds);

}
