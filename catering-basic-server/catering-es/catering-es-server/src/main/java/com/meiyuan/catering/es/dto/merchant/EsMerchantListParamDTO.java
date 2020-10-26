package com.meiyuan.catering.es.dto.merchant;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yaoozu
 * @description 在售商户列表查询参数
 * @date 2020/4/2118:06
 * @since v1.0.0
 */
@Data
@ApiModel("商户列表分页查询参数")
public class EsMerchantListParamDTO extends BasePageDTO {
    @ApiModelProperty(value = "城市编码",required = true)
    private String cityCode;
    @ApiModelProperty(value = "维度",required = true)
    private Double lat;
    @ApiModelProperty(value = "经度",required = true)
    private Double lng;
    @ApiModelProperty("商户名称")
    private String merchantName;
    @ApiModelProperty("查询条件 1-距离最近 2-好评优先 3-销量最高")
    private Integer searchType;
    @ApiModelProperty("品牌id")
    private Long brandId;
    @ApiModelProperty("商户id集合")
    private List<String> merchantIdList;
    @ApiModelProperty("店铺id集合【1.2.0】")
    private List<String> shopIdList;
    @ApiModelProperty(hidden = true, value = "是否查询菜单模式下的商家")
    private Boolean flag;
    @ApiModelProperty(hidden = true, value = "是否有可售卖商品")
    private Boolean haveGoodsFlag;
    @ApiModelProperty(hidden = true, value = "首页展示的商家个数")
    private Integer indexShowMerchantSize;
    @ApiModelProperty(hidden = true, value = "商家商品展示的个数")
    private Integer indexShowMerchantGoodsSize;
    @ApiModelProperty(value = "优惠活动类型 : 1：折扣商品，2：进店领券，3：满减优惠券，4，秒杀活动，5：团购活动【1.3.0】")
    private Integer activityType;
    @ApiModelProperty(value = "配送方式：1：仅配送，2：仅自提【1.3.0】")
    private Integer deliveryType;
    @ApiModelProperty(hidden = true,value = "是否是企业用户：true ：是")
    private Boolean companyUser;
    @ApiModelProperty("城市店铺总数")
    private Long cityShopSum;
}
