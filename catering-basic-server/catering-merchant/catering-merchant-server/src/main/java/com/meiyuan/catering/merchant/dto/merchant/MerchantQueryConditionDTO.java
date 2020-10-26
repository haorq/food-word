package com.meiyuan.catering.merchant.dto.merchant;

import com.meiyuan.catering.core.util.BaseUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 商户信息查询条件 参数DTO
 * @Date  2020/3/12 0012 16:20
 */
@Data
@ApiModel("商户信息查询条件 参数DTO")
public class MerchantQueryConditionDTO {

    @ApiModelProperty("商户ids")
    private List<Long> merchantIds;
    @ApiModelProperty("门店ids")
    private List<Long> shopIds;

    public List<Long> getMerchantIds() {
        if (BaseUtil.judgeList(merchantIds)){
            return merchantIds;
        }
        return merchantIds = new ArrayList<>();
    }

    public void setMerchantIds(List<Long> merchantIds) {
        this.merchantIds = merchantIds;
    }
}
