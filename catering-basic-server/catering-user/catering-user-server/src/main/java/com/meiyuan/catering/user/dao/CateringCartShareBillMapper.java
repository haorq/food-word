package com.meiyuan.catering.user.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.user.entity.CateringCartEntity;
import com.meiyuan.catering.user.entity.CateringCartShareBillEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description 拼单表数据库访问层
 * ${@link CateringCartShareBillEntity}
 * @author yaozou
 * @date 2020/3/25 14:18
 * @since v1.0.0
 */
@Mapper
public interface CateringCartShareBillMapper extends BaseMapper<CateringCartShareBillEntity>{


}