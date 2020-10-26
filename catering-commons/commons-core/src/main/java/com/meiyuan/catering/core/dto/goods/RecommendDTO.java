package com.meiyuan.catering.core.dto.goods;

import com.meiyuan.catering.core.page.BasePageDTO;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/9/7 11:54
 * @since v1.1.0
 */
@Data
public class RecommendDTO extends BasePageDTO {

    private String cityCode;
    private String location;
}
