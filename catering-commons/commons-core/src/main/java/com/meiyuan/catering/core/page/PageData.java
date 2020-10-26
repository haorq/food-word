package com.meiyuan.catering.core.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 分页工具类
 *
 * @author admin
 */
@Data
@ApiModel(value = "分页数据")
public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总记录数")
    private int total;

    @ApiModelProperty(value = "列表数据")
    private List<T> list = Collections.emptyList();
    @ApiModelProperty(value = "是否最后一页")
    private boolean lastPage = false;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public PageData(List<T> list, long total) {
        this.list = list;
        this.total = (int) total;
    }

    public PageData(List<T> list, long total, boolean lastPage) {
        this.list = list;
        this.total = (int) total;
        this.lastPage = lastPage;
    }

    public PageData(List<T> list, IPage page) {
        this.list = list;
        this.total = (int) page.getTotal();
        this.lastPage = page.getCurrent() >= page.getPages();
    }

    public PageData(IPage<T> page) {
        this.list = page.getRecords();
        this.total = (int) page.getTotal();
        this.lastPage = page.getCurrent() >= page.getPages();
    }

    public PageData() {
    }

    public void setLastPage(Long pageSize, Long pageNo) {
        if (pageSize == 0) {
            this.lastPage = true;
        } else {
            Long pages = total / pageSize;
            if (total % pageSize != 0) {
                pages++;
            }
            this.lastPage = Objects.equals(pageNo, pages);
        }
    }
}
