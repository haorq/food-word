package com.meiyuan.catering.goods.dto.goods;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Description 查询所有赠品参数接口DTO
 * @Date  2020/3/20 0020 10:40
 */
@ApiModel("查询所有赠品参数接口DTO")
@Data
public class GoodsGiftListDTO {
    @ApiModelProperty("赠品giftGoodIds")
    private List<IdEntity> giftGoodIds;
}
