package com.meiyuan.catering.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.dto.es.MerchantBaseLabel;
import com.meiyuan.catering.goods.dto.label.LabelDTO;
import com.meiyuan.catering.goods.dto.label.LabelDetailDTO;
import com.meiyuan.catering.goods.dto.label.LabelLimitQueryDTO;
import com.meiyuan.catering.goods.entity.CateringLabelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 标签表(CateringLabel)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-09 17:53:21
 */
@Mapper
public interface CateringLabelMapper extends BaseMapper<CateringLabelEntity> {

    /**
     * 方法描述
     *
     * @param goodsIds 商品id集合
     * @author: wxf
     * @date: 2020/6/23 10:54
     * @return: {@link List< LabelDetailDTO>}
     * @version 1.1.0
     **/
    List<LabelDetailDTO> getByGoodId(@Param("goodsIds") List<Long> goodsIds);

    /**
     * describe: 查询分页列表，并统计商品数量
     * @author: yy
     * @date: 2020/7/6 18:03
     * @param page
     * @param labelLimitQuery
     * @return: {com.baomidou.mybatisplus.core.metadata.IPage<com.meiyuan.catering.goods.dto.label.LabelDTO>}
     * @version 1.2.0
     **/
    IPage<LabelDTO> queryPageList(@Param("page") Page<LabelDTO> page, @Param("labelLimitQuery") LabelLimitQueryDTO labelLimitQuery);
    /**
     * 获取商户商品标签
     * @param merchantId
     * @param goodsIds
     * @return
     */
    List<LabelDetailDTO> listNameByGoodsId(@Param("merchantId") Long merchantId, @Param("list") List<Long> goodsIds);

    /**
     * 描述:查询商品标签
     *
     * @param goodsIds
     * @return java.util.List<com.meiyuan.catering.core.dto.es.MerchantBaseLabel>
     * @author zengzhangni
     * @date 2020/8/7 9:33
     * @since v1.3.0
     */
    List<MerchantBaseLabel> getLabel(@Param("goodsIds")Collection<Long> goodsIds);

}
