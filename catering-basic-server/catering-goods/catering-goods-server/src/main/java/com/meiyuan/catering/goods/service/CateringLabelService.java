package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.es.MerchantBaseLabel;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.goods.dto.label.LabelDTO;
import com.meiyuan.catering.goods.dto.label.LabelDetailDTO;
import com.meiyuan.catering.goods.dto.label.LabelLimitQueryDTO;
import com.meiyuan.catering.goods.entity.CateringLabelEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 标签表(CateringLabel)服务层
 *
 * @author wxf
 * @since 2020-03-09 18:05:09
 */
public interface CateringLabelService extends IService<CateringLabelEntity> {
    /**
     * 新增修改标签
     *
     * @author: wxf
     * @date: 2020/3/12 13:56
     * @param dto 新增修改数据DTO
     * @return: java.lang.String
     **/
    String saveUpdate(LabelDTO dto);

    /**
     * 标签列表分页
     *
     * @author: wxf
     * @date: 2020/5/19 15:28
     * @param dto 基础查询参数
     * @return: {@link  PageData< LabelDTO>}
     * @version 1.0.1
     **/
    PageData<LabelDTO> listLimit(LabelLimitQueryDTO dto);

    /**
     * 删除标签
     * 默认标签不能删除
     * @author: wxf
     * @date: 2020/5/19 15:31
     * @param id 标签id
     * @return: {@link  String}
     * @version 1.0.1
     **/
    String del(Long id);

    /**
     * 获取标签信息
     *
     * @author: wxf
     * @date: 2020/5/19 15:32
     * @param id 标签id
     * @return: {@link LabelDTO}
     * @version 1.0.1
     **/
    LabelDTO getById(Long id);

    /**
     * 全部标签
     *
     * @author: wxf
     * @date: 2020/3/21 11:16
     * @return: {@link List< LabelDTO>}
     * @version 1.0.1
     **/
    List<LabelDTO> allLabel();

    /**
     * 通过goodId获取标签集合
     *
     * @author: wxf
     * @date: 2020/6/23 10:54
     * @param goodsId 商品id
     * @return: {@link List< LabelDetailDTO>}
     * @version 1.1.0
     **/
    List<LabelDetailDTO> getByGoodId(List<Long> goodsId);

    /**
     * 获取商户商品标签
     * @param merchantId
     * @param goodsIds
     * @return
     */
    Map<Long,List<String>> listNameByGoodsId(Long merchantId, List<Long> goodsIds);

    /**
     * 描述:查询商品标签
     *
     * @param goodsIds
     * @return java.util.List<com.meiyuan.catering.core.dto.es.MerchantBaseLabel>
     * @author zengzhangni
     * @date 2020/8/7 9:35
     * @since v1.3.0
     */
    List<MerchantBaseLabel> getLabel(Collection<Long> goodsIds);
}
