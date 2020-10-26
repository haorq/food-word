package com.meiyuan.catering.merchant.dto.merchant;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Description 商户回收站列表分页查询参数DTO
 * @Date  2020/3/16 0016 14:53
 */
@Data
@ApiModel("商户回收站列表分页查询参数DTO")
public class MerchantRecyclePageQueryDTO extends BasePageDTO {

    @ApiModelProperty("关键字：联系人姓名，手机号")
    private String keyword;
    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("省编码")
    private String addressProvinceCode;
    @ApiModelProperty("市编码")
    private String addressCityCode;
    @ApiModelProperty("区编码")
    private String addressAreaCode;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

}
