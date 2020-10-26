package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.DateTimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName MerchantAppGoodsDetailsDTO
 * @Description 商户app商品详情DTO
 * @Author gz
 * @Date 2020/7/8 17:00
 * @Version 1.2.0
 */
@Data
@ApiModel("商户app商品详情--app")
public class MerchantAppGoodsDetailsDTO {
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("extendId")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long extendId;
    @ApiModelProperty("goodsId")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("菜品编号")
    private String spuCode;
    @ApiModelProperty("菜品名称")
    private String goodsName;
    @ApiModelProperty("菜品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("标签名集合")
    private List<String> labelNameList;
    @ApiModelProperty("标签id集合")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> labelIdList;
    @ApiModelProperty("详情图片集合")
    private List<String> infoPictureList;
    @ApiModelProperty(value = "详情图片str",hidden = true)
    private String infoPicture;
    @ApiModelProperty("商品详细介绍，是富文本格式")
    private String goodsDescribeText;
    @ApiModelProperty("商品简介")
    private String goodsSynopsis;
    @ApiModelProperty(value = "是否开启预售")
    private Boolean presellFlag;
    @ApiModelProperty("开始售卖时间")
    private LocalDate startSellTime;
    @ApiModelProperty("结束售卖时间")
    private LocalDate endSellTime;

    private String sellWeekTime;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("规格集合")
    private List<MerchantAppGoodsSkuDTO> skuList;

    @ApiModelProperty("是否参加活动： true 是 flase 否")
    private Boolean isJoinActivity;


    public List<String> getInfoPictureList(){
        if(StringUtils.isNotBlank(this.infoPicture)){
            return Arrays.asList(this.infoPicture.split(","));
        }
        return Collections.EMPTY_LIST;
    }
    @ApiModelProperty(value = "售卖星期")
    public List<String> getSellWeekTime(){
        if(StringUtils.isNotBlank(this.sellWeekTime)){
            return DateTimeUtil.weekString(this.sellWeekTime);
        }
        return Collections.EMPTY_LIST;
    }
}
