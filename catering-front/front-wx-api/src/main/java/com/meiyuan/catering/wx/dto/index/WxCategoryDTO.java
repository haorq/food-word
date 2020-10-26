package com.meiyuan.catering.wx.dto.index;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.dto.base.RedisWxCategoryDTO;
import com.meiyuan.catering.core.enums.base.RelevanceTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author zengzhangni
 * @date 2020/5/6
 */
@Data
@NoArgsConstructor
public class WxCategoryDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 类目名称
     */
    private String name;
    /**
     * 类目图标
     */
    private String icon;
    /**
     * 跳转链接
     */
    @ApiModelProperty("linkType=3  link=appid")
    private String link;
    @ApiModelProperty("跳转类型:1:现有地址 2:自定义跳转 3:跳转至第三方地址")
    private Integer linkType;
    /**
     * 品牌故事图片
     */
    private String storyUrl;
    /**
     * 品牌故事描述
     */
    private String storyDescribe;
    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 是否关联了(商户 、 商家)/配置了二级跳转地址
     */
    private Boolean hasStory;

    /**
     * 关联类型 1:商家 2:商品 3:入驻商家
     */
    @ApiModelProperty("关联类型 0:无 1:商家 2:商品 3:入住商家")
    private Integer relevanceType;

    /**
     * @description 关联个数(商户 、 商家)
     * @author yaozou
     * @date 2020/5/20 14:34
     * @param
     * @return
     * @since v1.0.1
     */
    private Integer relevanceNum;

    public WxCategoryDTO(RedisWxCategoryDTO categoryDTO) {
        this.setId(categoryDTO.getId());
        this.setName(categoryDTO.getName());
        this.setIcon(categoryDTO.getIcon());
        this.setLink(categoryDTO.getLink());
        this.setLinkType(categoryDTO.getLinkType());
        this.setSort(categoryDTO.getSort());
        this.setRelevanceType(categoryDTO.getRelevanceType());
        int relevanceNum;
        if (Objects.equals(categoryDTO.getRelevanceType(), RelevanceTypeEnum.MERCHANT.getStatus())) {
            //1:商家
            relevanceNum = categoryDTO.getStoryList() == null ? 0 : categoryDTO.getStoryList().size();
        } else if (Objects.equals(categoryDTO.getRelevanceType(), RelevanceTypeEnum.GOODS.getStatus())) {
            //商品
            relevanceNum = categoryDTO.getStoryGoodsList() == null ? 0 : categoryDTO.getStoryGoodsList().size();
        } else if (Objects.equals(categoryDTO.getRelevanceType(), RelevanceTypeEnum.ENTER_MERCHANT.getStatus())) {
            //入驻商家
            relevanceNum = categoryDTO.getImgList() == null ? 0 : categoryDTO.getImgList().size();
        } else {
            //信息流
            relevanceNum = categoryDTO.getWxCategoryExtList() == null ? 0 : categoryDTO.getWxCategoryExtList().size();
        }

        boolean hasStory;
        // 1:现有地址 2:自定义跳转
        if (categoryDTO.getLinkType() == 1) {
            hasStory = !StringUtils.isEmpty(categoryDTO.getLink());
        } else {
            hasStory = relevanceNum > 0;
        }
        this.setHasStory(hasStory);
        this.setRelevanceNum(relevanceNum);
    }

}
