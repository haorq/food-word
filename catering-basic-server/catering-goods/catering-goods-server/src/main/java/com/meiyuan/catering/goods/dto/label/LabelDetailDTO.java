package com.meiyuan.catering.goods.dto.label;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lhm
 * @date 2020/6/4
 * @description
 **/
@Data
public class LabelDetailDTO {
    private List<String> labelName;
    private Long goodsId;
}
