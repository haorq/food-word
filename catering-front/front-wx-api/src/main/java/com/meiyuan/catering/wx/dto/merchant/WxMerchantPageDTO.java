package com.meiyuan.catering.wx.dto.merchant;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Description WX商户列表搜索条件
 * @Date  2020/5/6 0006 15:29
 */
@Data
@ApiModel("WX商户列表搜索条件")
public class WxMerchantPageDTO extends BasePageDTO {
    @ApiModelProperty(value = "位置：经度,纬度")
    private String location;
    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("排序方式：1.距离最近,2.好评优先,3.销量最高")
    private Integer sortType;
}
