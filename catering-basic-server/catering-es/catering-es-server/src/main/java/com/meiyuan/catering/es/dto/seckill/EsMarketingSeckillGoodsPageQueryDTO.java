package com.meiyuan.catering.es.dto.seckill;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author GongJunZheng
 * @date 2020/08/08 11:08
 * @description 小程序限时秒杀指定场次商品分页列表查询DTO
 **/

@Data
@ApiModel(value = "小程序限时秒杀指定场次商品分页列表查询DTO")
public class EsMarketingSeckillGoodsPageQueryDTO extends BasePageDTO {

    @ApiModelProperty(value = "秒杀场次ID", required = true)
    @NotNull(message = "秒杀场次ID不能为空")
    private Long eventId;
    @ApiModelProperty(value = "位置：经度,纬度", required = true)
    @NotBlank(message = "位置经纬度不能为空")
    private String location;
    @ApiModelProperty(value = "市编码", required = true)
    @NotBlank(message = "市编码不能为空")
    private String cityCode;
    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户",hidden = true)
    private Integer userType;

    @ApiModelProperty(value = "秒杀活动场次状态 3：已开枪  4：正在疯抢  2：即将开抢  6：明日预告")
    private Integer status;

}
