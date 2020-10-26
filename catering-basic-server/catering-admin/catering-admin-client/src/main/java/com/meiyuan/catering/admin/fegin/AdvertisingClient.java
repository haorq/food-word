package com.meiyuan.catering.admin.fegin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.admin.dto.advertising.AdvertisingListQueryDTO;
import com.meiyuan.catering.admin.dto.advertising.AdvertisingSaveDTO;
import com.meiyuan.catering.admin.entity.CateringAdvertisingEntity;
import com.meiyuan.catering.admin.service.CateringAdvertisingService;
import com.meiyuan.catering.admin.vo.advertising.AdvertisingDetailVO;
import com.meiyuan.catering.core.dto.admin.advertising.AdvertisingListVo;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/5/19 9:57
 **/
@Service
public class AdvertisingClient {
    @Autowired
    private CateringAdvertisingService cateringAdvertisingService;

    /**
     * @return {@link Result< IPage< CateringAdvertisingEntity>>}
     * @Author lhm
     * @Description 管理员列表分页查询
     * @Date 2020/5/19
     * @Param [dto]
     * @Version v1.1.0
     */
    public Result<PageData<AdvertisingListVo>> pageList(AdvertisingListQueryDTO dto) {
        return Result.succ(cateringAdvertisingService.pageList(dto));
    }

    /**
     * describe: 刷新redis广告
     * @author: yy
     * @date: 2020/9/9 13:52
     * @return: {@link }
     * @version 1.4.0
     **/
    public void resetShowAdvertising() {
        cateringAdvertisingService.resetShowAdvertisingAndRemove();
    }

    /**
     * 描述:更新广告显示状态
     *
     * @param time
     * @param show
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/21 16:16
     * @since v1.1.0
     */
    public Result<Boolean> updateShow(Boolean show, LocalDateTime time) {
        return Result.succ(cateringAdvertisingService.updateShow(show, time));
    }

    /**
     * describe: 添加/编辑
     *
     * @param dto
     * @author: yy
     * @date: 2020/9/2 15:25
     * @return: {@link Result< Boolean>}
     * @version 1.4.0
     **/
    public Result<Boolean> saveOrUpdate(AdvertisingSaveDTO dto) {
        return Result.succ(cateringAdvertisingService.saveOrUpdate(dto));
    }

    /**
     * describe: 删除广告
     *
     * @param id
     * @author: yy
     * @date: 2020/9/2 15:25
     * @return: {@link Result< Boolean>}
     * @version 1.4.0
     **/
    public Result<Boolean> deleteById(Long id) {
        return Result.succ(cateringAdvertisingService.deleteById(id));
    }

    /**
     * describe: 查询详情
     *
     * @param id
     * @author: yy
     * @date: 2020/9/3 11:16
     * @return: {@link Result<  AdvertisingDetailVO >}
     * @version 1.4.0
     **/
    public Result<AdvertisingDetailVO> queryDetailById(Long id) {
        return Result.succ(cateringAdvertisingService.queryDetailById(id));
    }
}
