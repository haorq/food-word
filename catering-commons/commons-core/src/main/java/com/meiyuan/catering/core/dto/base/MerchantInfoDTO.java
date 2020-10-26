package com.meiyuan.catering.core.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Description 商户店铺信息
 * @Date  2020/3/24 0024 14:03
 */
@Data
public class MerchantInfoDTO implements Serializable {
    private static final long serialVersionUID = 6772196836835198653L;
    /**
     * 商户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "商户编码")
    private String merchantCode;

    @ApiModelProperty(value = "商户/品牌 名称")
    private String merchantName;

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

    @ApiModelProperty(value = "审核状态:1:待上传，2：未审核，3：已通过，4：未通过")
    private Integer auditStatus;

    @ApiModelProperty(value = "商户状态：1-启用 2- 禁用 ")
    private Integer merchantStatus;

    @ApiModelProperty(value = " 是否删除： 0-否 1-是")
    private Boolean del;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
}
