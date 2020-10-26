package com.meiyuan.catering.user.dto.sharebill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 拼单信息查询
 * @date 2020/5/1914:57
 * @since v1.0.0
 */
@Data
public class ShareBillQueryDTO {
    public ShareBillQueryDTO(){}
    public ShareBillQueryDTO(String shareBillNo,Boolean userInfoFlag){
        this.shareBillNo = shareBillNo;
        this.userInfoFlag = userInfoFlag;
    }

    private String shareBillNo;
    @ApiModelProperty("是否查询拼单人信息")
    private Boolean userInfoFlag;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Long merchantId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    private Integer type;
}
