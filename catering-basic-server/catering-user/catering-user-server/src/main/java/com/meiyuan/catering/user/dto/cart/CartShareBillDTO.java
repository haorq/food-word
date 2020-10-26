package com.meiyuan.catering.user.dto.cart;

import lombok.Data;

import java.util.List;

/**
 * @author yaoozu
 * @description 拼单信息
 * @date 2020/3/2617:23
 * @since v1.0.0
 */
@Data
public class CartShareBillDTO {
    private CartShareBillBaseDTO shareBill;
    private List<CartShareBillUserDTO> shareBillUserList;
}
