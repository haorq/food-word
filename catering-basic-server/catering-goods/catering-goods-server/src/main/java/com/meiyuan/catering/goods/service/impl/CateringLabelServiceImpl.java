package com.meiyuan.catering.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.meiyuan.catering.core.dto.es.MerchantBaseLabel;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.core.enums.base.InsertUpdateDelNameCountSizeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.goods.dao.CateringLabelMapper;
import com.meiyuan.catering.goods.dto.es.CategoryLabelDelToEsDTO;
import com.meiyuan.catering.goods.dto.label.LabelDTO;
import com.meiyuan.catering.goods.dto.label.LabelDetailDTO;
import com.meiyuan.catering.goods.dto.label.LabelLimitQueryDTO;
import com.meiyuan.catering.goods.entity.CateringGoodsLabelRelationEntity;
import com.meiyuan.catering.goods.entity.CateringLabelEntity;
import com.meiyuan.catering.goods.enums.CategoryLabelStatusEnum;
import com.meiyuan.catering.goods.enums.CategoryLabelTypeEnum;
import com.meiyuan.catering.goods.enums.DefaultEnum;
import com.meiyuan.catering.goods.mq.sender.GoodsSenderMq;
import com.meiyuan.catering.goods.service.CateringGoodsLabelRelationService;
import com.meiyuan.catering.goods.service.CateringLabelService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标签表(CateringLabel)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringLabelServiceImpl extends ServiceImpl<CateringLabelMapper, CateringLabelEntity>
        implements CateringLabelService {
    @Resource
    private CateringLabelMapper cateringLabelMapper;
    @Resource
    CateringGoodsLabelRelationService labelRelationService;
    @Resource
    GoodsSenderMq goodsSenderMq;
    @Value("${goods.merchant.id}")
    private Long goodsMerchantId;

    /**
     * 新增修改标签
     *
     * @param dto 新增修改数据DTO
     * @author: wxf
     * @date: 2020/3/12 13:56
     * @return: java.lang.String
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveUpdate(LabelDTO dto) {
        String returnString;
        CateringLabelEntity entity = BaseUtil.objToObj(dto, CateringLabelEntity.class);
        String labelName = dto.getLabelName();
        // null 新增 反则 修改
        if (null == dto.getId()) {
            validationLabelName(Boolean.TRUE, labelName, null);
            long id = IdWorker.getId();
            entity.setId(id);
            entity.setMerchantId(1L);
            entity.setDefaultLabel(DefaultEnum.ADD.getStatus());
            entity.setLabelStatus(CategoryLabelStatusEnum.YES.getStatus());
            int insertSize = cateringLabelMapper.insert(entity);
            returnString = BaseUtil.insertUpdateDelSetString(insertSize, "新增成功", "新增失败");
        } else {
            validationLabelName(Boolean.FALSE, labelName, entity.getId());
            boolean flag = this.updateById(entity);
            if (flag) {
                CategoryLabelDelToEsDTO esDto = new CategoryLabelDelToEsDTO();
                esDto.setId(entity.getId());
                esDto.setName(entity.getLabelName());
                esDto.setType(CategoryLabelTypeEnum.LABEL.getStatus());
                esDto.setDefaultFlag(Boolean.FALSE);
                esDto.setUpdateOrDel(Boolean.TRUE);
                goodsSenderMq.categoryLabelDel(esDto);
            }
            returnString = BaseUtil.insertUpdateDelBatchSetString(flag, "修改成功", "修改失败");
        }
        return returnString;
    }

    /**
     * 验证 标签名称
     *
     * @param saveOrUpdate        新增还是修改
     * @param saveUpdateLabelName 新增修改标签名称
     * @param id                  标签id
     * @author: wxf
     * @date: 2020/3/12 11:00
     * @version 1.0.1
     **/
    private void validationLabelName(boolean saveOrUpdate, String saveUpdateLabelName, Long id) {
        int labelNameCount = labelNameCount(saveUpdateLabelName);
        if (saveOrUpdate) {
            if (InsertUpdateDelNameCountSizeEnum.SIZE.getStatus() <= labelNameCount) {
                throw new CustomException("新增标签名字重复");
            }
        } else {
            CateringLabelEntity label = cateringLabelMapper.selectById(id);
            if (null == label) {
                throw new CustomException("修改类目ID不存在");
            }
            // 不一样
            if (!label.getLabelName().equals(saveUpdateLabelName)) {
                if (!InsertUpdateDelNameCountSizeEnum.ZERO.getStatus().equals(labelNameCount)) {
                    throw new CustomException("修改标签名字存在");
                }
            }
        }
    }

    /**
     * 标签名字统计
     *
     * @param labelName 标签名称
     * @author: wxf
     * @date: 2020/3/12 11:20
     * @return: {@link Integer}
     * @version 1.0.1
     **/
    private Integer labelNameCount(String labelName) {
        QueryWrapper<CateringLabelEntity> queryWrapper = publicQueryWrapper();
        queryWrapper.lambda().eq(CateringLabelEntity::getLabelName, labelName);
        return cateringLabelMapper.selectCount(queryWrapper);
    }

    /**
     * 公共查询的QueryWrapper
     *
     * @author: wxf
     * @date: 2020/5/19 15:17
     * @return: {@link QueryWrapper< CateringLabelEntity>}
     * @version 1.0.1
     **/
    private QueryWrapper<CateringLabelEntity> publicQueryWrapper() {
        QueryWrapper<CateringLabelEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringLabelEntity::getMerchantId, goodsMerchantId)
                .eq(CateringLabelEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return queryWrapper;
    }

    /**
     * 标签列表分页
     *
     * @param dto 基础查询参数
     * @author: wxf
     * @date: 2020/5/19 15:28
     * @return: {@link  PageData< LabelDTO>}
     * @version 1.0.1
     **/
    @Override
    public PageData<LabelDTO> listLimit(LabelLimitQueryDTO dto) {
        Page<LabelDTO> page2 = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<LabelDTO> iPage = cateringLabelMapper.queryPageList(page2, dto);
        return new PageData<>(iPage.getRecords(), (int) iPage.getTotal());
    }

    /**
     * 删除标签
     * 默认标签不能删除
     *
     * @param id 标签id
     * @author: wxf
     * @date: 2020/5/19 15:31
     * @return: {@link  String}
     * @version 1.0.1
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String del(Long id) {
        if (null == id) {
            throw new CustomException("找不到对应标签！");
        }
        QueryWrapper<CateringLabelEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringLabelEntity::getId, id);
        CateringLabelEntity labelEntity = cateringLabelMapper.selectOne(queryWrapper);
        if (labelEntity == null) {
            return "标签已删除！";
        }
        labelEntity.setDel(DelEnum.DELETE.getFlag());
        // 逻辑删除
        boolean flag = this.updateById(labelEntity);
        if (!flag) {
            throw new CustomException("标签删除失败！");
        }
        QueryWrapper<CateringGoodsLabelRelationEntity> updateQueryWrapper = new QueryWrapper<>();
        updateQueryWrapper.lambda().eq(CateringGoodsLabelRelationEntity::getLabelId, id);
        // 得到受影响的商品
        List<CateringGoodsLabelRelationEntity> updateGoodsLabelList = labelRelationService.list(updateQueryWrapper);
        String returnString = "删除成功！";
        if (CollectionUtils.isEmpty(updateGoodsLabelList)) {
            return returnString;
        }
        // 没有默认标签直接删除
        boolean bool = labelRelationService.remove(updateQueryWrapper);
        if(!bool){
            throw new CustomException("商品关联标签删除失败！");
        }
        List<Long> goodsIdList =
                updateGoodsLabelList.stream().map(CateringGoodsLabelRelationEntity::getGoodsId).collect(Collectors.toList());
        CategoryLabelDelToEsDTO dto = new CategoryLabelDelToEsDTO();
        dto.setGoodsList(goodsIdList);
        dto.setId(labelEntity.getId());
        dto.setDefaultFlag(Boolean.TRUE);
        dto.setName(labelEntity.getLabelName());
        dto.setType(CategoryLabelTypeEnum.LABEL.getStatus());
        dto.setUpdateOrDel(Boolean.FALSE);
        goodsSenderMq.categoryLabelDel(dto);
        return returnString;
    }

    /**
     * 获取标签信息
     *
     * @param id 标签id
     * @author: wxf
     * @date: 2020/5/19 15:32
     * @return: {@link LabelDTO}
     * @version 1.0.1
     **/
    @Override
    public LabelDTO getById(Long id) {
        CateringLabelEntity label = cateringLabelMapper.selectById(id);
        if (null == label) {
            throw new CustomException("ID对应标签不存在");
        }
        return BaseUtil.objToObj(label, LabelDTO.class);
    }

    /**
     * 全部标签
     *
     * @author: wxf
     * @date: 2020/3/21 11:16
     * @return: {@link List < LabelDTO>}
     * @version 1.0.1
     **/
    @Override
    public List<LabelDTO> allLabel() {
        List<LabelDTO> list = Collections.emptyList();
        QueryWrapper<CateringLabelEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CateringLabelEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        List<CateringLabelEntity> labelList = cateringLabelMapper.selectList(queryWrapper);
        if (BaseUtil.judgeList(labelList)) {
            list = BaseUtil.objToObj(labelList, LabelDTO.class);
        }
        return list;
    }

    @Override
    public List<LabelDetailDTO> getByGoodId(List<Long> goodsIds) {

        return cateringLabelMapper.getByGoodId(goodsIds);
    }

    @Override
    public Map<Long, List<String>> listNameByGoodsId(Long merchantId, List<Long> goodsIds) {
        List<LabelDetailDTO> dtos = cateringLabelMapper.listNameByGoodsId(merchantId, goodsIds);
        Map<Long, List<String>> resMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(dtos)) {
            dtos.forEach(e -> {
                resMap.put(e.getGoodsId(), e.getLabelName());
            });
        }
        return resMap;
    }

    @Override
    public List<MerchantBaseLabel> getLabel(Collection<Long> goodsIds) {
        return baseMapper.getLabel(goodsIds);
    }
}
