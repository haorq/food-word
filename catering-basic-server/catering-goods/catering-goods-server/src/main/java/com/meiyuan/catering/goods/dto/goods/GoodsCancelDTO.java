package com.meiyuan.catering.goods.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.aspectj.lang.annotation.DeclareAnnotation;

/**
 * @author lhm
 * @date 2020/7/8
 * @description
 **/
@Data
@ApiModel("取消授权dto")
public class GoodsCancelDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
}
