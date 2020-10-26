package com.meiyuan.catering.merchant.vo.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author MeiTao
 * @Date 2020/7/2 0002 17:55
 * @Description 简单描述 : 商户平台列表分页
 * @Since version-1.2.0
 */
@Data
@ApiModel("商户平台列表分页VO")
public class MerchantPageVo{

    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("商户名称")
    private String merchantName;

    @ApiModelProperty("商户编码")
    private String merchantCode;

    @ApiModelProperty("来源：1- 商户申请 2-平台添加")
    private Integer merchantSource;

    @ApiModelProperty("注册电话")
    private String phone;

    @ApiModelProperty("法人姓名")
    private String legalPersonName;

    @ApiModelProperty("品牌属性:1:自营，2：非自营")
    private Integer merchantAttribute;

    @ApiModelProperty("商户服务:1;外卖小程序,2:食堂美食城（json）")
    private String merchantService;

    @ApiModelProperty("审核状态:1:待上传，2：未审核，3：已通过，4：未通过")
    private Integer auditStatus;

    @ApiModelProperty("商户状态：1- 禁用 2-启用")
    private Integer merchantStatus;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("商户账号/用户名【1.3.0】")
    private String account;

}
