package com.meiyuan.catering.goods.dto.label;

import lombok.Data;
import org.aspectj.lang.annotation.DeclareAnnotation;

import java.util.List;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/8/5 10:41
 */
@Data
public class LabelGoodDTO {
    private Long goodsId;
    private List<String> labelName;
    private List<Long> labelId;
}
