package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dao.CateringMarketingPickupPointMapper;
import com.meiyuan.catering.marketing.dto.pickup.*;
import com.meiyuan.catering.marketing.entity.CateringMarketingPickupPointEntity;
import com.meiyuan.catering.marketing.entity.CateringMarketingPickupPointShopRelationEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingPickupPointService;
import com.meiyuan.catering.marketing.service.CateringMarketingPickupPointShopRelationService;
import com.meiyuan.catering.marketing.vo.pickup.PickupCiftGoodInfoVO;
import com.meiyuan.catering.marketing.vo.pickup.PickupGiftListVO;
import com.meiyuan.catering.marketing.vo.pickup.ShopGiftVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luohuan
 * @date 2020/3/19
 **/
@Service("cateringMarketingPickupPointService")
public class CateringMarketingPickupPointServiceImpl extends ServiceImpl<CateringMarketingPickupPointMapper, CateringMarketingPickupPointEntity>
        implements CateringMarketingPickupPointService {
    @Resource
    private CateringMarketingPickupPointMapper pickupPointMapper;

    @Resource
    private CateringMarketingPickupPointShopRelationService pickupPointShopRelationService;

    @Override
    public Result<IPage<PickupGiftListVO>> listPickupGiftPage(PickupGiftPageQueryDTO queryDTO) {
        if(!ObjectUtils.isEmpty(queryDTO.getEndTime())){
            queryDTO.setEndTime(queryDTO.getEndTime().plusDays(1));
        }
        IPage<PickupGiftListVO> listPickupGiftPage = getBaseMapper().listPickupGiftPage(queryDTO.getPage(), queryDTO);
        return Result.succ(listPickupGiftPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertPickupGiftActivity(PickupGiftActivityAddDTO dto) {
        CateringMarketingPickupPointEntity pickupPointEntity = new CateringMarketingPickupPointEntity();
        BeanUtils.copyProperties(dto, pickupPointEntity);
        //添加自提点活动表
        getBaseMapper().insert(pickupPointEntity);
        //添加自提点活动与赠品关联关系表
        Long id = CodeGenerator.createId();
        getBaseMapper().insertPickupPointGifRelation(id,dto.getGiftId(), pickupPointEntity.getId());
        //添加自提点活动与与商户关联关系表
        if (!BaseUtil.judgeList(dto.getShopList())){
            throw new CustomException(501,"店铺信息不能为空");
        }

        dto.getShopList().forEach(shop->{
            shop.setId(CodeGenerator.createId());
        });
        getBaseMapper().insertPickupPointShopRelation(dto.getShopList(),pickupPointEntity.getId());
        return Result.succ();
    }

    @Override
    public List<PickupCiftGoodInfoVO> listPickupActivityGoodId(Long id) {
        return getBaseMapper().listPickupActivityGoodId(id);
    }

    @Override
    public List<PickupGiftShopDTO> listActivityShop(Long id) {
        return getBaseMapper().listActivityShop(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PickupRemainGiftDTO> delPickupActivity(Long id) {
        removeById(id);
        return getBaseMapper().listPickupRemainGifts(id);
    }

    @Override
    public List<Long> listGiftActivitySame( Long giftId) {
        List<CateringMarketingPickupPointEntity> pickupPointEntities = pickupPointMapper.listGiftActivitySame(giftId);

        //若当前赠品无任何对应的活动，则所有店铺都可参加
        if (!BaseUtil.judgeList(pickupPointEntities)){
            return null;
        }

        List<Long> activityIds = pickupPointEntities.stream().map(CateringMarketingPickupPointEntity::getId).collect(Collectors.toList());

        //通过活动id查询参与商户
        List<Long> shopIds = null;
        if(BaseUtil.judgeList(activityIds)){
            shopIds = pickupPointMapper.listShopId(activityIds);
        }
        return shopIds;
    }
    @Override
    public IPage<ShopGiftVO> listGiftByShop(Long pageNo,Long pageSize,Long shopId,List<Long> giftIds) {
        return pickupPointMapper.listGiftByShop(new Page(pageNo, pageSize), shopId,giftIds);
    }

    @Override
    public List<ShopGiftVO> listShopGift(List<Long> pickupId, Long shopId) {
        return pickupPointMapper.listShopGift(pickupId,shopId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShopGiftStock(List<GiftUpdateStockDTO> dto, Long shopId, Boolean type) {
        //用户下订单，暂时不对赠品库存进行操作
        if (!ObjectUtils.isEmpty(shopId)){
           return;
        }

        //查询对应赠品
        List<Long> pickupIds = dto.stream().map(GiftUpdateStockDTO::getPickupId).collect(Collectors.toList());

        QueryWrapper<CateringMarketingPickupPointShopRelationEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringMarketingPickupPointShopRelationEntity::getShopId,shopId)
                .in(CateringMarketingPickupPointShopRelationEntity::getPickupId,pickupIds);
        List<CateringMarketingPickupPointShopRelationEntity> pickupPointShopEntities = pickupPointShopRelationService.list(query);

        if (!BaseUtil.judgeList(pickupPointShopEntities)){
            throw new CustomException(501,"店铺赠品不存在");
        }

        Map<Long, GiftUpdateStockDTO> map = dto.stream().collect(Collectors.toMap(GiftUpdateStockDTO::getPickupId, giftUpdateStockDTO -> giftUpdateStockDTO));

        //type true:减少库存，false:恢复库存
        if (type) {
            pickupPointShopEntities.forEach(entity -> {
                GiftUpdateStockDTO giftUpdateStockDTO = map.get(entity.getPickupId());
                Integer giftRemainQuantity = entity.getGiftRemainQuantity() - giftUpdateStockDTO.getNumber();
                entity.setGiftRemainQuantity(giftRemainQuantity);
            });
            //减少商家对应赠品库存（店铺赠送赠品库存可以为负）
            pickupPointShopRelationService.updateBatchById(pickupPointShopEntities);
        }else {
            pickupPointShopEntities.forEach(entity -> {
                GiftUpdateStockDTO giftUpdateStockDTO = map.get(entity.getPickupId());
                Integer giftRemainQuantity = entity.getGiftRemainQuantity() + giftUpdateStockDTO.getNumber();
                entity.setGiftRemainQuantity(giftRemainQuantity);
            });
            //恢复商家对应赠品库存
            pickupPointShopRelationService.updateBatchById(pickupPointShopEntities);
        }
        log.debug("店铺自提订单赠品库存已修改");
    }
}
