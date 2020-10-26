package com.meiyuan.catering.merchant.dto.shop.bank;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/9/29 0029 18:18
 * @Description 简单描述 : 
 * @Since version-1.5.0
 */
@ApiModel(value="店铺结算信息添加DTO", description="")
@Data
public class ShopNameAuthDTO{
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @ApiModelProperty(value = "店铺结算信息id,实名认证时不传")
        private Long id;
        @ApiModelProperty(value = "店铺id")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long shopId;

        @ApiModelProperty(value = "用户姓名")
        private String userName;

        @ApiModelProperty(value = "用户真实身份证号")
        private String idCard;

        @ApiModelProperty(value = "短信验证码")
        private String code;
        @ApiModelProperty(value = "手机号")
        private String phone;
        @ApiModelProperty(value = "签约状态：1-实名认证、2-电子签约、3-绑定银行卡、4-已完成")
        private Integer auditStatus;
}







