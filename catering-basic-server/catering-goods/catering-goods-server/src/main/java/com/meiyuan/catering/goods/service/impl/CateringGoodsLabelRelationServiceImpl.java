package com.meiyuan.catering.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dao.CateringGoodsLabelRelationMapper;
import com.meiyuan.catering.goods.dto.es.GoodsEsGoodsDTO;
import com.meiyuan.catering.goods.dto.goods.GoodsCategoryAndLabelDTO;
import com.meiyuan.catering.goods.dto.label.LabelGoodDTO;
import com.meiyuan.catering.goods.dto.label.LabelRelationDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsLabelRelationEntity;
import com.meiyuan.catering.goods.entity.CateringLabelEntity;
import com.meiyuan.catering.goods.enums.CategoryLabelTypeEnum;
import com.meiyuan.catering.goods.enums.DefaultEnum;
import com.meiyuan.catering.goods.service.CateringGoodsLabelRelationService;
import com.meiyuan.catering.goods.service.CateringLabelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 标签商品关联表(CateringGoodsLabelRelation)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringGoodsLabelRelationServiceImpl extends ServiceImpl<CateringGoodsLabelRelationMapper, CateringGoodsLabelRelationEntity>
        implements CateringGoodsLabelRelationService {
    @Resource
    private CateringGoodsLabelRelationMapper cateringGoodsLabelRelationMapper;
    @Resource
    private CateringLabelService labelService;


    /**
     * 获取标签id和标签名称
     *
     * @param goodsIdList 商品id集合
     * @author: wxf
     * @date: 2020/3/23 20:38
     * @return: {@link List <  GoodsCategoryAndLabelDTO >}
     **/
    @Override
    public List<GoodsCategoryAndLabelDTO> listByGoodsIdList(Long merchantId, List<Long> goodsIdList) {
        return cateringGoodsLabelRelationMapper.listByGoodsIdList(merchantId, goodsIdList);
    }


    @Override
    public List<GoodsCategoryAndLabelDTO> listByGoodsIdListAndMerchant(List<Long> goodsIdList, Long merchantId) {
        return cateringGoodsLabelRelationMapper.listByGoodsIdListAndMerchant(goodsIdList, merchantId);
    }

    /**
     * 批量获取根据标签id
     *
     * @param labelId 标签id
     * @author: wxf
     * @date: 2020/5/19 15:55
     * @return: {@link List<  LabelRelationDTO >}
     * @version 1.0.1
     **/
    @Override
    public List<LabelRelationDTO> listByLabelId(Long labelId) {
        QueryWrapper<CateringGoodsLabelRelationEntity> labelRelationQueryWrapper = new QueryWrapper<>();
        labelRelationQueryWrapper.lambda().eq(CateringGoodsLabelRelationEntity::getLabelId, labelId).eq(CateringGoodsLabelRelationEntity::getMerchantId, 1L);
        List<CateringGoodsLabelRelationEntity> labelList = this.list(labelRelationQueryWrapper);
        List<LabelRelationDTO> dtoList = Collections.emptyList();
        if (BaseUtil.judgeList(labelList)) {
            dtoList = BaseUtil.objToObj(labelList, LabelRelationDTO.class);
        }
        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGoodsLabel(List<Long> labelIdList, long goodsId, Long merchantId, GoodsEsGoodsDTO esGoodsDTO) {
        // 先删除后新增
        QueryWrapper<CateringGoodsLabelRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(CateringGoodsLabelRelationEntity::getMerchantId)
                .eq(CateringGoodsLabelRelationEntity::getGoodsId, goodsId);

        List<Long> list = this.listObjs(queryWrapper, o -> Long.valueOf(o.toString()));
        List<CateringGoodsLabelRelationEntity> labelRelationList1 = new ArrayList<>();
        Set<Long> merchantIdList = null;
        boolean flag = true;
        if (CollectionUtils.isNotEmpty(list)) {
            flag = remove(queryWrapper);
            merchantIdList = new HashSet<>(list);
        } else {
            merchantIdList = this.baseMapper.selectMerchantIdsByGoodsId(goodsId);
            //平台创建的
            if (merchantId.equals(1L)) {
                merchantIdList.add(merchantId);
            }
        }
        if (BaseUtil.judgeList(labelIdList)) {
            merchantIdList.forEach(e -> {
                List<CateringGoodsLabelRelationEntity> labelRelationList = labelIdList.stream().map(
                        i -> {
                            CateringGoodsLabelRelationEntity labelRelation = new CateringGoodsLabelRelationEntity();
                            labelRelation.setId(IdWorker.getId());
                            labelRelation.setGoodsId(goodsId);
                            labelRelation.setLabelId(i);
                            labelRelation.setMerchantId(e);
                            return labelRelation;
                        }
                ).collect(Collectors.toList());
                labelRelationList1.addAll(labelRelationList);
            });
            flag = saveBatch(labelRelationList1);
        }

        if (flag && !ObjectUtils.isEmpty(esGoodsDTO)) {
            List<GoodsCategoryAndLabelDTO> dtoList = new ArrayList<>();
            if (BaseUtil.judgeList(labelIdList)) {
                QueryWrapper<CateringLabelEntity> labelWrapper = new QueryWrapper<>();
                labelWrapper.lambda().in(CateringLabelEntity::getId, labelIdList);
                List<CateringLabelEntity> labelList = labelService.list(labelWrapper);
                if (BaseUtil.judgeList(labelList)) {
                    GoodsCategoryAndLabelDTO label;
                    dtoList = new ArrayList<>();
                    for (CateringLabelEntity entity : labelList) {
                        if (DefaultEnum.ADD.getStatus().equals(entity.getDefaultLabel())) {
                            label = new GoodsCategoryAndLabelDTO();
                            label.setType(CategoryLabelTypeEnum.LABEL.getStatus());
                            label.setId(entity.getId());
                            label.setName(entity.getLabelName());
                            dtoList.add(label);
                        }
                    }

                }
            }
            esGoodsDTO.setLabelList(dtoList);
        }
    }

    @Override
    public LabelGoodDTO getLabelList(Long merchantId, List<Long> goodsIds) {
        return cateringGoodsLabelRelationMapper.getLabelList(merchantId, goodsIds);

    }

}
