package com.meiyuan.catering.wx.vo;

import com.meiyuan.catering.wx.dto.advertising.WxAdvertisingIndexDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述: 首页广告展示
 *
 * @author zengzhangni
 * @date 2020/9/2 9:29
 * @since v1.3.0
 */
@Data
@ApiModel("首页广告展示DTO")
public class WxAdvertisingVO implements Serializable {

    @ApiModelProperty("顶部广告")
    private List<WxAdvertisingIndexDTO> advertisingList;

    @ApiModelProperty("中部广告")
    private List<WxAdvertisingIndexDTO> centerAdvertisingList;
}
