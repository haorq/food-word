package com.meiyuan.catering.admin.dto.role;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName AdminRolePageDTO
 * @Description
 * @Author gz
 * @Date 2020/9/28 10:15
 * @Version 1.5.0
 */
@Data
public class AdminRolePageDTO extends BasePageDTO {
    @ApiModelProperty(value = "创建时间起")
    private LocalDateTime createTimeBegin;
    @ApiModelProperty(value = "创建时间止")
    private LocalDateTime createTimeEnd;
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "选中的角色Id集合")
    private List<Long> roleIdList;
}
