package com.meiyuan.catering.core.page;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Project: saas-cloud
 * @Package com.meiyuan.commons.tools.page
 * @author : luojianhong
 * @date Date : 2019年10月17日 10:40
 * @version V1.0
 * 分页基础类
 */
@Data
public class BasePageDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "当前页",required = true)
    private Long pageNo;

    @ApiModelProperty(value = "每页大小",required = true)
    private Long pageSize;

    @ApiModelProperty(hidden = true)
    public Page getPage() {
        return new Page(pageNo, pageSize);
    }

    public BasePageDTO() {
        this.pageNo = 1L;
        this.pageSize = 20L;
    }
}
