package com.meiyuan.catering.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.admin.dao.CateringAdvertisingMapper;
import com.meiyuan.catering.admin.dto.advertising.AdvertisingExtSaveDTO;
import com.meiyuan.catering.admin.dto.advertising.AdvertisingListQueryDTO;
import com.meiyuan.catering.admin.dto.advertising.AdvertisingSaveDTO;
import com.meiyuan.catering.admin.entity.CateringAdvertisingEntity;
import com.meiyuan.catering.admin.entity.CateringAdvertisingExtEntity;
import com.meiyuan.catering.admin.enums.base.DelEnum;
import com.meiyuan.catering.admin.enums.base.PublishTypeEnum;
import com.meiyuan.catering.admin.service.CateringAdvertisingExtService;
import com.meiyuan.catering.admin.service.CateringAdvertisingService;
import com.meiyuan.catering.admin.util.AdminCacheUtil;
import com.meiyuan.catering.admin.vo.advertising.AdvertisingDetailVO;
import com.meiyuan.catering.core.dto.admin.advertising.AdvertisingListVo;
import com.meiyuan.catering.core.dto.base.RedisAdvertisingDTO;
import com.meiyuan.catering.core.enums.base.AdvertisingLinkEnum;
import com.meiyuan.catering.core.enums.base.AdvertisingLinkTypeEnum;
import com.meiyuan.catering.core.enums.base.AdvertisingShowsEnum;
import com.meiyuan.catering.core.enums.base.WxCategoryStatusEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 广告服务类
 *
 * @author zengzhangni
 * @date 2020-03-19
 */
@Service
public class CateringAdvertisingServiceImpl extends ServiceImpl<CateringAdvertisingMapper, CateringAdvertisingEntity> implements CateringAdvertisingService {

    @Resource
    private AdminCacheUtil adminCacheUtil;
    @Resource
    private CateringAdvertisingExtService cateringAdvertisingExtService;

    @Override
    public PageData<AdvertisingListVo> pageList(AdvertisingListQueryDTO dto) {
        //处理关键字
        String name = CharUtil.disposeChar(dto.getName());
        LocalDateTime startTime = dto.getStartTime();
        LocalDateTime endTime = dto.getEndTime();
        Boolean enabled = dto.getEnabled();
        Integer position = dto.getPosition();
        LambdaQueryWrapper<CateringAdvertisingEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(enabled != null, CateringAdvertisingEntity::getEnabled, enabled)
                .ge(startTime != null, CateringAdvertisingEntity::getCreateTime, startTime)
                .lt(endTime != null, CateringAdvertisingEntity::getCreateTime, endTime != null ? endTime.plusDays(1) : null)
                .and(StringUtils.isNotBlank(name), w -> w.like(CateringAdvertisingEntity::getName, name))
                .eq(position != null, CateringAdvertisingEntity::getPosition,position)
                .orderByAsc(CateringAdvertisingEntity::getSort);

        IPage<CateringAdvertisingEntity> page = baseMapper.selectPage(dto.getPage(), wrapper);

        List<AdvertisingListVo> vos = page.getRecords().stream().map(record -> {
            AdvertisingListVo vo = new AdvertisingListVo();
            BeanUtils.copyProperties(record, vo);
            return vo;
        }).collect(Collectors.toList());
        return new PageData<>(vos, page.getTotal());
    }

    @Override
    public void resetShowAdvertising() {
        LambdaQueryWrapper<CateringAdvertisingEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringAdvertisingEntity::getEnabled, WxCategoryStatusEnum.ENABLE.getFlag())
               .eq(CateringAdvertisingEntity::getShows, true);
        List<AdvertisingDetailVO> detailList = this.baseMapper.queryDetailAll();
        if(CollectionUtils.isEmpty(detailList)){
            return;
        }
        adminCacheUtil.putAdvertising(detailList);
    }

    @Override
    public void resetShowAdvertisingAndRemove() {
        adminCacheUtil.delAdvertising();
        this.resetShowAdvertising();
    }

    @Override
    public Boolean updateShow(Boolean show, LocalDateTime time) {
        return baseMapper.updateShow(show, time);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(AdvertisingSaveDTO dto) {
        if (null == dto) {
            throw new CustomException("保存参数接收不到！");
        }
        CateringAdvertisingEntity entity = ConvertUtils.sourceToTarget(dto, CateringAdvertisingEntity.class);
        // 禁用
        Boolean enabled = entity.getEnabled();
        entity.setShows(enabled);
        Integer publishType = entity.getPublishType();
        // 预约发布-禁用不生效
        if (PublishTypeEnum.SUBSCRIBE.getStatus().equals(publishType) && Boolean.TRUE.equals(enabled)) {
            LocalDateTime nowTime = LocalDateTime.now();
            LocalDateTime startTime = entity.getStartTime();
            LocalDateTime endTime = entity.getEndTime();
            if (startTime == null || endTime == null) {
                throw new CustomException("发布时间不能为空！");
            }
            if (nowTime.isAfter(startTime)) {
                throw new CustomException("预约发布开始时间不能小于等于当前时间！");
            }
            if (nowTime.isAfter(endTime)) {
                throw new CustomException("预约发布结束时间不能小于当前时间！");
            }
            if (startTime.isAfter(endTime)) {
                throw new CustomException("预约发布开始时间不能大于结束时间！");
            }
            entity.setShows(AdvertisingShowsEnum.NOT_SHOW.getFlag());
        }
        boolean flag = this.saveOrUpdate(entity);
        if (!flag) {
            throw new CustomException("广告数据更新失败！");
        }
        Long advertisingId = dto.getId();
        if (null != advertisingId) {
            QueryWrapper<CateringAdvertisingExtEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(CateringAdvertisingExtEntity::getAdvertisingId, advertisingId);
            cateringAdvertisingExtService.remove(queryWrapper);
        }
        Long id = entity.getId();
        List<AdvertisingExtSaveDTO> advertisingExtList = dto.getAdvertisingExtList();
        if (CollectionUtils.isNotEmpty(advertisingExtList)) {
            List<CateringAdvertisingExtEntity> extEntityList = advertisingExtList.stream().map(e -> {
                CateringAdvertisingExtEntity extEntity = ConvertUtils.sourceToTarget(e, CateringAdvertisingExtEntity.class);
                extEntity.setId(IdWorker.getId());
                extEntity.setAdvertisingId(id);
                return extEntity;
            }).collect(Collectors.toList());
            cateringAdvertisingExtService.saveBatch(extEntityList);
        }
        AdvertisingDetailVO detail = this.queryDetailById(id);
        return this.addRedis(detail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        if (null == id) {
            throw new CustomException("删除的广告编号不存在！");
        }
        CateringAdvertisingEntity entity = this.getById(id);
        Boolean enabled = entity.getEnabled();
        if (WxCategoryStatusEnum.ENABLE.getFlag().equals(enabled)) {
            throw new CustomException("启用状态的广告不能删除！");
        }
        UpdateWrapper<CateringAdvertisingEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(CateringAdvertisingEntity::getDel, DelEnum.DELETE.getFlag()).eq(CateringAdvertisingEntity::getId, id);
        boolean flag = this.update(updateWrapper);
        if (flag) {
            adminCacheUtil.removeAdvertising(id);
        }
        return flag;
    }

    @Override
    public AdvertisingDetailVO queryDetailById(Long id) {
        return this.baseMapper.queryDetailById(id);
    }

    /**
     * describe: 添加缓存
     * @author: yy
     * @date: 2020/9/8 15:22
     * @param detail
     * @return: {@link boolean}
     * @version 1.4.0
     **/
    private boolean addRedis(AdvertisingDetailVO detail){
        if(null == detail){
            return false;
        }
        Boolean enabled = detail.getEnabled();
        Boolean shows = detail.getShows();
        if (WxCategoryStatusEnum.DISABLE.getFlag().equals(enabled) || Boolean.FALSE.equals(shows)) {
            return adminCacheUtil.removeAdvertising(detail.getId());
        }
        String linkStr = detail.getLinkStr();
        Integer linkType = detail.getLinkType();
        if(AdvertisingLinkTypeEnum.CUSTOMIZE.getStatus().equals(linkType)){
            if(CollectionUtils.isEmpty(detail.getAdvertisingExtList())){
                linkStr = null;
            }else{
                linkStr = AdvertisingLinkTypeEnum.CUSTOMIZE.getUrl() + detail.getId();
            }
        }else if (AdvertisingLinkTypeEnum.INSIDE.getStatus().equals(linkType)){
            String link = detail.getLink();
            if(AdvertisingLinkEnum.APPOINT_SHOP.getStatus().equals(link)){
                linkStr = linkStr + "?shopId=" + detail.getShopId();
            }else if(AdvertisingLinkEnum.APPOINT_GOODS.getStatus().equals(link)){
                linkStr = linkStr + "?shopId=" + detail.getShopId() + "&goodsId="+detail.getGoodsId()+"&showDetail=true";
            }
        }else {
            linkStr = null;
        }
        detail.setLink(linkStr);
        RedisAdvertisingDTO dto = BaseUtil.objToObj(detail, RedisAdvertisingDTO.class);
        adminCacheUtil.putAdvertising(dto);
        return true;
    }
}