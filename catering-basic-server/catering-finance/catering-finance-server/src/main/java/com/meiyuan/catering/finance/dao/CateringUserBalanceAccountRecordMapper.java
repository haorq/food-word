package com.meiyuan.catering.finance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.finance.entity.CateringUserBalanceAccountRecordEntity;
import com.meiyuan.catering.finance.vo.account.ConsumeRefundVO;
import com.meiyuan.catering.finance.vo.account.IncomeExpendVO;
import com.meiyuan.catering.finance.vo.account.UserAccountRecordListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-16
 */
@Mapper
public interface CateringUserBalanceAccountRecordMapper extends BaseMapper<CateringUserBalanceAccountRecordEntity> {

    /**
     * 描述: 总收入/总支出
     *
     * @param userId 用户id
     * @return com.meiyuan.catering.finance.vo.account.IncomeExpendVO
     * @author zengzhangni
     * @date 2020/6/23 10:47
     * @since v1.1.1
     */
    IncomeExpendVO incomeExpendByUserId(@Param("userId") Long userId);

    /**
     * 描述:查询账户消费记录列表
     *
     * @param page   分页条件
     * @param userId 用户id
     * @return java.util.List<com.meiyuan.catering.finance.vo.account.UserAccountRecordListVO>
     * @author zengzhangni
     * @date 2020/6/23 10:47
     * @since v1.1.1
     */
    List<UserAccountRecordListVO> queryAccountRecordList(Page page, @Param("userId") Long userId);

    /**
     * 描述: 已退款金额/已消费金额
     *
     * @param userId 用户id
     * @return com.meiyuan.catering.finance.vo.account.ConsumeRefundVO
     * @author zengzhangni
     * @date 2020/6/23 10:47
     * @since v1.1.1
     */
    ConsumeRefundVO consumeRefundByUserId(Long userId);
}
