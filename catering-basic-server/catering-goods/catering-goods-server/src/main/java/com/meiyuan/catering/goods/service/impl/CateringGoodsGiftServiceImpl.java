package com.meiyuan.catering.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dao.CateringGoodsGiftMapper;
import com.meiyuan.catering.goods.dto.gift.*;
import com.meiyuan.catering.goods.entity.CateringGoodsGiftEntity;
import com.meiyuan.catering.goods.service.CateringGoodsGiftService;
import com.meiyuan.catering.goods.util.GoodsUtil;
import com.meiyuan.catering.goods.vo.goods.GoodsGiftListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 赠品表(CateringGoodsGift)表服务实现类
 *
 * @author wxf
 * @since 2020-03-18 18:38:38
 */
@Service("cateringGoodsGiftService")
public class CateringGoodsGiftServiceImpl extends ServiceImpl<CateringGoodsGiftMapper, CateringGoodsGiftEntity>
        implements CateringGoodsGiftService {
    @Resource
    private CateringGoodsGiftMapper goodsGiftMapper;
    @Resource
    GoodsUtil goodsUtil;
    @Value("${goods.merchant.id}")
    private Long goodsMerchantId;


    /**
     * 新增修改
     *
     * @author: wxf
     * @date: 2020/5/19 11:58
     * @param dto 新增修改赠品数据
     * @return: {@link  String}
     * @version 1.0.1
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveUpdate(GiftDTO dto) {
        String returnString;
        CateringGoodsGiftEntity gift = BaseUtil.objToObj(dto, CateringGoodsGiftEntity.class);
        // null 新增 反则 修改
        if (null == dto.getId()) {
            String merchant = String.valueOf(goodsMerchantId);
            gift.setId(IdWorker.getId());
            gift.setGiftCode(goodsUtil.giftCode(merchant, zpCodeMaxInteger(merchant)));
            int insertSize = goodsGiftMapper.insert(gift);
            returnString = BaseUtil.insertUpdateDelSetString(insertSize, "新增成功", "新增失败");
        } else {
            int updateSize = goodsGiftMapper.updateById(gift);
            returnString = BaseUtil.insertUpdateDelSetString(updateSize, "修改成功", "修改失败");
        }
        return returnString;
    }

    /**
     * 赠品列表
     *
     * @author: wxf
     * @date: 2020/5/19 14:49
     * @param dto 列表查询参数
     * @return: {@link PageData< GiftDTO>}
     * @version 1.0.1
     **/
    @Override
    public PageData<GiftDTO> listLimit(GiftLimitQueryDTO dto) {
        QueryWrapper<CateringGoodsGiftEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsGiftEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        if (BaseUtil.judgeString(dto.getGiftNameCode())) {
            queryWrapper.lambda().like(CateringGoodsGiftEntity::getGiftName, dto.getGiftNameCode());
        }
        if (null != dto.getGiftStatus()) {
            queryWrapper.lambda().eq(CateringGoodsGiftEntity::getGiftStatus, dto.getGiftStatus());
        }
        queryWrapper.lambda().orderByDesc(CateringGoodsGiftEntity::getCreateTime);
        Page<CateringGoodsGiftEntity> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        goodsGiftMapper.selectPage(page, queryWrapper);
        PageData<GiftDTO> pageData = new PageData<>();
        if (BaseUtil.judgeList(page.getRecords())) {
            pageData.setList(BaseUtil.objToObj(page.getRecords(), GiftDTO.class));
        }
        pageData.setTotal((int) page.getTotal());
        return pageData;
    }

    /**
     * 删除
     *
     * @author: wxf
     * @date: 2020/5/19 14:51
     * @param giftId 赠品id
     * @return: {@link  String}
     * @version 1.0.1
     **/
    @Override
    public String del(Long giftId) {
        QueryWrapper<CateringGoodsGiftEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringGoodsGiftEntity::getId, giftId)
                .eq(CateringGoodsGiftEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        CateringGoodsGiftEntity gift = goodsGiftMapper.selectOne(queryWrapper);
        String returnString;
        if (null != gift) {
            gift.setDel(DelEnum.DELETE.getFlag());
            int updateSize = goodsGiftMapper.updateById(gift);
            returnString = BaseUtil.insertUpdateDelSetString(updateSize, "删除成功", "删除失败");
        } else {
            returnString = "赠品已被删除";
        }
        return returnString;
    }

    /**
     * 赠品查询所有
     *
     * @author: wxf
     * @date: 2020/5/19 14:53
     * @param ids 查询条件参数
     * @return: {@link List< GiftDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<GiftDTO> listGiftGood(List<Long> ids) {
        QueryWrapper<CateringGoodsGiftEntity> wra = new QueryWrapper<>();
        wra.lambda().eq(CateringGoodsGiftEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        //若为传入商品id空则查询所有
        if (BaseUtil.judgeList(ids)) {
            wra.lambda().in(CateringGoodsGiftEntity::getId,ids);
        }
        List<GiftDTO> dtoList = Collections.emptyList();
        List<CateringGoodsGiftEntity> entityList = goodsGiftMapper.selectList(wra);
        if (BaseUtil.judgeList(entityList)) {
            dtoList = BaseUtil.objToObj(entityList, GiftDTO.class);
        }
        return dtoList;
    }

    /**
     * 查询所有赠品
     *
     * @author: wxf
     * @date: 2020/5/19 14:56
     * @return: {@link List< GoodsGiftListVo>}
     * @version 1.0.1
     **/
    @Override
    public List<GoodsGiftListVo> listShop() {
        QueryWrapper<CateringGoodsGiftEntity> wra = new QueryWrapper<>();
        wra.lambda().eq(CateringGoodsGiftEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringGoodsGiftEntity> goodsGifts = goodsGiftMapper.selectList(wra);
        List<GoodsGiftListVo> goodsGiftList = new ArrayList<>();
        goodsGifts.forEach(goodsGift -> {
            GoodsGiftListVo goodsGiftListVo = new GoodsGiftListVo();
            BeanUtils.copyProperties(goodsGift, goodsGiftListVo);
            goodsGiftList.add(goodsGiftListVo);
        });
        return goodsGiftList;
    }

    /**
     * 减少赠品库存
     * 进行库存验证
     * @author: wxf
     * @date: 2020/5/19 15:05
     * @param pickupGiftGoods 参数
     * @version 1.0.1
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reduceGiftGoodStock(List<GiftGoodStockReduceDTO> pickupGiftGoods) {

        // 更改库存的赠品id集合
        List<Long> giftIdList = pickupGiftGoods.stream().map(GiftGoodStockReduceDTO::getGiftId).collect(Collectors.toList());
        if (!BaseUtil.judgeList(giftIdList)) {
            throw new CustomException("赠品id为空");
        }
        List<CateringGoodsGiftEntity> giftList = goodsGiftMapper.selectBatchIds(giftIdList);

        // key 赠品id
        Map<Long, CateringGoodsGiftEntity> giftMap = giftList.stream().collect(
                Collectors.toMap(CateringGoodsGiftEntity::getId, Function.identity(), (oldValue, newValue) -> oldValue)
        );
        //更新赠品库存
        updateGiftStock(pickupGiftGoods, giftMap);

    }

    /**
     * 更新赠品库存
     *
     * @param pickupGiftGoods
     * @param giftMap
     */
    private synchronized void updateGiftStock(List<GiftGoodStockReduceDTO> pickupGiftGoods, Map<Long, CateringGoodsGiftEntity> giftMap) {
        List<CateringGoodsGiftEntity> saveList = pickupGiftGoods.stream().map(
                i -> {
                    CateringGoodsGiftEntity entity = new CateringGoodsGiftEntity();
                    Long giftId = i.getGiftId();
                    CateringGoodsGiftEntity gift = giftMap.getOrDefault(giftId, null);
                    entity.setId(giftId);
                    Long giftQuantity = Long.valueOf(i.getGiftQuantity());
                    // 验证库存 <0
                    if (gift.getGiftStock() < giftQuantity) {
                        throw new CustomException("赠品库存不足：" + gift.getGiftName() + "当前库存 ：" + gift.getGiftStock());
                    }
                    entity.setGiftStock(gift.getGiftStock() - giftQuantity);
                    return entity;
                }
        ).collect(Collectors.toList());

        this.saveOrUpdateBatch(saveList);
    }

    /**
     * 增加商品库存
     *
     * @author: wxf
     * @date: 2020/5/19 15:07
     * @param goodsGiftId 赠品ID
     * @param giftQuantity 赠品数量
     * @version 1.0.1
     **/
    @Override
    public void increaseGiftGoodStock(Long goodsGiftId, Long giftQuantity) {
        CateringGoodsGiftEntity giftEntity = getById(goodsGiftId);
        if (giftEntity != null) {
            synchronized (this) {
                Long stock = giftEntity.getGiftStock() + giftQuantity;
                giftEntity.setGiftStock(stock);
                updateById(giftEntity);
            }
        }
    }

    /**
     * 赠品详情
     *
     * @author: wxf
     * @date: 2020/3/21 17:49
     * @param giftId 赠品id
     * @return: {@link GiftDTO}
     * @version 1.0.1
     **/
    @Override
    public GiftDTO getGiftInfoById(Long giftId) {
        if (null == giftId) {
            throw new CustomException("id为空");
        }
        CateringGoodsGiftEntity gift = goodsGiftMapper.selectById(giftId);
        if (null == gift) {
            throw new CustomException("id对应赠品不存在");
        }
        return BaseUtil.objToObj(gift, GiftDTO.class);
    }

    /**
     * 方法描述 : 通过查询条件查询赠品信息
     * @Author: MeiTao
     * @Date: 2020/5/22 0022 9:29
     * @param dto
     * @return: java.util.List<com.meiyuan.catering.goods.dto.gift.GiftAllDTO>
     * @Since version-1.0.0
     */
    @Override
    public List<GiftAllDTO> listShopGiftGood(GiftAllDTO dto){
        //根据赠品名称查询对应赠品id
        QueryWrapper<CateringGoodsGiftEntity> query = new QueryWrapper<>();
        query.lambda().eq(CateringGoodsGiftEntity::getDel,DelEnum.NOT_DELETE);
        if(BaseUtil.judgeString(dto.getGiftName())){
            String disposeChar = CharUtil.disposeChar(dto.getGiftName());
            query.lambda().like(CateringGoodsGiftEntity::getGiftName,disposeChar);
        }
        List<CateringGoodsGiftEntity> goodsGiftEntities = goodsGiftMapper.selectList(query);

        return listGiftAllDTO(goodsGiftEntities);
    }


    @Override
    public Result<List<GiftAllDTO>> listGiftGoods(GoodsGiftDTO dto){
        QueryWrapper<CateringGoodsGiftEntity> query = new QueryWrapper<>();
        query.lambda().in(BaseUtil.judgeList(dto.getIds()),CateringGoodsGiftEntity::getId,dto.getIds());
        List<CateringGoodsGiftEntity> goodsGiftEntities = goodsGiftMapper.selectList(query);
        return Result.succ(listGiftAllDTO(goodsGiftEntities));
    }

    private List<GiftAllDTO> listGiftAllDTO(List<CateringGoodsGiftEntity> goodsGiftEntities){
        List<GiftAllDTO> result = new ArrayList<>();
        goodsGiftEntities.forEach(giftEntity ->
            result.add(ConvertUtils.sourceToTarget(giftEntity,GiftAllDTO.class))
        );
        return result;
    }

    private Integer zpCodeMaxInteger(String merchant) {
        CateringGoodsGiftEntity entity = goodsGiftMapper.maxDbCode();
        if (null == entity) {
            return 0;
        }
        String giftCode = entity.getGiftCode();
        return Integer.valueOf(giftCode.split("ZP" + merchant)[1]);
    }
}