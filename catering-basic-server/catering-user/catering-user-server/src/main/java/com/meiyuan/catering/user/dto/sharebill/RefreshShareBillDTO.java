package com.meiyuan.catering.user.dto.sharebill;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author yaoozu
 * @description 刷新拼单信息
 * @date 2020/5/1914:48
 * @since v1.1.0
 */
@Data
public class RefreshShareBillDTO {
    private String shareBillNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long   userId;
}
