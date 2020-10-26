package com.meiyuan.catering.goods.feign;

import com.meiyuan.catering.core.dto.es.MerchantBaseLabel;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.label.LabelDTO;
import com.meiyuan.catering.goods.dto.label.LabelLimitQueryDTO;
import com.meiyuan.catering.goods.service.CateringLabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author wxf
 * @date 2020/5/19 15:13
 * @description 简单描述
 **/
@Service
public class LabelClient {
    @Resource
    CateringLabelService labelService;

    /**
     * 新增修改标签
     *
     * @author: wxf
     * @date: 2020/5/19 15:19
     * @param dto 新增修改数据DTO
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
   public Result<String> saveUpdate(LabelDTO dto) {
       return Result.succ(labelService.saveUpdate(dto));
   }

    /**
     * 标签列表分页
     *
     * @author: wxf
     * @date: 2020/5/19 15:28
     * @param dto 基础查询参数
     * @return: {@link Result< PageData< LabelDTO>>}
     * @version 1.0.1
     **/
    public Result<PageData<LabelDTO>> listLimit(LabelLimitQueryDTO dto) {
        return Result.succ(labelService.listLimit(dto));
    }

    /**
     * 删除标签
     * 默认标签不能删除
     * @author: wxf
     * @date: 2020/5/19 15:31
     * @param id 标签id
     * @return: {@link Result< String>}
     * @version 1.0.1
     **/
    public Result<String> del(Long id) {
        return Result.succ(labelService.del(id));
    }

    /**
     * 获取标签信息
     *
     * @author: wxf
     * @date: 2020/5/19 15:32
     * @param id 标签id
     * @return: {@link Result< LabelDTO>}
     * @version 1.0.1
     **/
    public Result<LabelDTO> getById(Long id) {
        return Result.succ(labelService.getById(id));
    }

    /**
     * 全部标签
     *
     * @author: wxf
     * @date: 2020/3/21 11:16
     * @return: {@link List < LabelDTO>}
     * @version 1.0.1
     **/
    public Result<List<LabelDTO>> allLabel() {
        return Result.succ(labelService.allLabel());
    }

    /**
     * 获取商户标签
     * @param merchantId 商户id
     * @param goodsIds 商品id
     * @return
     */
    public Map<Long,List<String>> listNameByGoodsId(Long merchantId, List<Long> goodsIds){
        return labelService.listNameByGoodsId(merchantId,goodsIds);
    }

    /**
     * 描述:查询商品标签
     *
     * @param goodsIds
     * @return java.util.List<com.meiyuan.catering.core.dto.es.MerchantBaseLabel>
     * @author zengzhangni
     * @date 2020/8/7 9:32
     * @since v1.3.0
     */
    public List<MerchantBaseLabel> getLabel(Collection<Long> goodsIds) {
        return labelService.getLabel(goodsIds);
    }
}
