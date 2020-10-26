package com.meiyuan.catering.merchant.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.merchant.vo.merchant.MerchantShopTagsVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author MeiTao
 * @Description 商户DTO
 * @Date  2020/3/12 0012 16:20
 */
@Data
@ApiModel("商户实体DTO")
public class MerchantDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "商户id修改时必传")
    private Long id;

    @ApiModelProperty(value = "商户编码")
    private String merchantCode;

    @ApiModelProperty(value = "商户/品牌 名称")
    private String merchantName;

    @ApiModelProperty(value = "来源：1- 平台添加 2-商户申请")
    private Integer merchantSource;

    @ApiModelProperty(value = "注册电话")
    private String phone;

    @ApiModelProperty(value = "法人姓名")
    private String legalPersonName;

    @ApiModelProperty(value = "品牌属性:1:自营，2：非自营")
    private Integer merchantAttribute;

    @ApiModelProperty(value = "商户服务:1;外卖小程序,2:堂食美食城（json）")
    private String merchantService;

    @ApiModelProperty(value = "营业执照")
    private String businessLicense;

    @ApiModelProperty(value = "卫生许可证")
    private String hygieneLicense;

    @ApiModelProperty(value = "身份证正面")
    private String idCardPositive;

    @ApiModelProperty(value = "身份证反面")
    private String idCardBack;

    @ApiModelProperty(value = "审核状态:1:待上传，2：未审核，3：已通过，4：未通过")
    private Integer auditStatus;

    @ApiModelProperty(value = "品牌故事/商家描述(富文本)")
    private String merchantDesc;

    @ApiModelProperty(value = "商户状态：1- 启用 2-禁用")
    private Integer merchantStatus;

    @ApiModelProperty(value = " 是否删除： 0-否 1-是")
    private Boolean del;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("品牌标签")
    private List<MerchantShopTagsVo> merchantShopTags;
    @ApiModelProperty("品牌标签ids")
    private List<Long> merchantTagIds;
}
