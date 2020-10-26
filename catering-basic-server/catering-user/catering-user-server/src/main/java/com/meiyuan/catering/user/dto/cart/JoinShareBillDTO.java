package com.meiyuan.catering.user.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author yaoozu
 * @description 加入拼单
 * @date 2020/5/1914:13
 * @since v1.1.0
 */
@Data
public class JoinShareBillDTO {
    private String shareBillNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    private String avatar;
    private String nickname;
}
