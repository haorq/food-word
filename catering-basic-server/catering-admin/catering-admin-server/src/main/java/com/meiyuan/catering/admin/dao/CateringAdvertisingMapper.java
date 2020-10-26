package com.meiyuan.catering.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.admin.entity.CateringAdvertisingEntity;
import com.meiyuan.catering.admin.vo.advertising.AdvertisingDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020-03-19
 */
@Mapper
public interface CateringAdvertisingMapper extends BaseMapper<CateringAdvertisingEntity> {

    /**
     * 描述:更新广告
     *
     * @param show
     * @param time
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/25 17:26
     * @since v1.1.0
     */
    Boolean updateShow(@Param("show") Boolean show, @Param("time") LocalDateTime time);

    /**
     * describe: 查询详情
     * @author: yy
     * @date: 2020/9/3 11:24
     * @param id
     * @return: {@link AdvertisingDetailVO}
     * @version 1.4.0
     **/
    AdvertisingDetailVO queryDetailById(@Param("id") Long id);

    /**
     * describe: 查询所有广告
     * @author: yy
     * @date: 2020/9/9 13:48
     * @return: {@link List< AdvertisingDetailVO>}
     * @version 1.4.0
     **/
    List<AdvertisingDetailVO> queryDetailAll();
}
