package com.meiyuan.catering.wx.dto.advertising;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * @author yaoozu
 * @description 首页广告展示
 * @date 2020/3/2114:08
 * @since v1.0.0
 */
@Data
@ApiModel("首页广告展示DTO")
public class WxAdvertisingIndexDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "广告标题")
    private String name;

    @ApiModelProperty(value = "跳转的页面地址")
    private String link;

    @ApiModelProperty(value = "广告宣传图片")
    private String url;

    @ApiModelProperty(value = "广告位置：1:顶部 2:中部")
    private Integer position;

    @ApiModelProperty(value = "活动内容")
    private String content;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

/*    @Override
    public int compare(WxAdvertisingIndexDTO o1, WxAdvertisingIndexDTO o2) {
        Integer sort1 = o1.getSort();
        Integer sort2 = o2.getSort();
        if(sort1 == null){
            return -1;
        }
        if(sort2 == null){
            return 1;
        }
        return sort1.compareTo(sort2);
    }*/
}
