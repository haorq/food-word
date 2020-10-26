package com.meiyuan.catering.user.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.user.entity.CateringAddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 收货地址表(CateringAddress)表数据库访问层
 *
 * @author mei-tao
 * @since 2020-03-10 15:29:17
 */
@Mapper
public interface CateringAddressMapper extends BaseMapper<CateringAddressEntity>{



}
