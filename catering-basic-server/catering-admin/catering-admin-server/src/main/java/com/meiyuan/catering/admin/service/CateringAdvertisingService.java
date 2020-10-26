package com.meiyuan.catering.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.admin.dto.advertising.AdvertisingListQueryDTO;
import com.meiyuan.catering.admin.dto.advertising.AdvertisingSaveDTO;
import com.meiyuan.catering.admin.entity.CateringAdvertisingEntity;
import com.meiyuan.catering.admin.vo.advertising.AdvertisingDetailVO;
import com.meiyuan.catering.core.dto.admin.advertising.AdvertisingListVo;
import com.meiyuan.catering.core.page.PageData;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020-03-19
 */
public interface CateringAdvertisingService extends IService<CateringAdvertisingEntity> {

    /**
     * 分页列表
     *
     * @param dto
     * @return
     */
    PageData<AdvertisingListVo> pageList(AdvertisingListQueryDTO dto);

    /**
     * 重置显示的广告
     */
    void resetShowAdvertising();


    /**
     * 描述:删除所有key 重新缓存
     *
     * @param
     * @return void
     * @author zengzhangni
     * @date 2020/6/3 14:57
     * @since v1.1.0
     */
    void resetShowAdvertisingAndRemove();

    /**
     * 描述:更新广告显示状态
     *
     * @param show
     * @param time
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/21 16:16
     * @since v1.1.0
     */
    Boolean updateShow(Boolean show, LocalDateTime time);

    /**
     * describe: 添加/编辑
     * @author: yy
     * @date: 2020/9/2 16:01
     * @param dto
     * @return: {@link Boolean}
     * @version 1.4.0
     **/
    Boolean saveOrUpdate(AdvertisingSaveDTO dto);

    /**
     * describe: 删除广告
     * @author: yy
     * @date: 2020/9/2 16:02
     * @param id
     * @return: {@link Boolean}
     * @version 1.4.0
     **/
    Boolean deleteById(Long id);

    /**
     * describe: 查询详情
     * @author: yy
     * @date: 2020/9/3 11:17
     * @param id
     * @return: {@link AdvertisingDetailVO}
     * @version 1.4.0
     **/
    AdvertisingDetailVO queryDetailById(Long id);
}
