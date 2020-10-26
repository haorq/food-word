package com.meiyuan.catering.goods.dto.menu;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wxf
 * @date 2020/3/20 10:24
 * @description 简单描述
 **/
@Data
@ApiModel("商品菜单列表分页查询参数模型")
public class GoodsMenuLimitQueryDTO extends BasePageDTO {
    @ApiModelProperty("菜单名称")
    private String menuName;
    @ApiModelProperty("上架时间")
    private LocalDate upperShelfTime;
    @ApiModelProperty("下架时间")
    private LocalDate lowerShelfTime;
    @ApiModelProperty("菜单：1-下架,2-上架,3-全部")
    private Integer menuStatus;

    @ApiModelProperty("商品：1-下架,2-上架")
    private Integer goodsStatus;
    @ApiModelProperty(value = "时间点",hidden = true)
    private String date;
    @ApiModelProperty(value = "结束时间点（查一个时间段的菜单时）",hidden = true)
    private String endDate;
    @ApiModelProperty(value = "商户Ids",hidden = true)
    private String merchantIds;

    @ApiModelProperty(value = "商户Id",hidden = true)
    private Long merchantId;

    @ApiModelProperty("类型：3：全部，2：出售中，1：已下架")
    private Integer type;
}
