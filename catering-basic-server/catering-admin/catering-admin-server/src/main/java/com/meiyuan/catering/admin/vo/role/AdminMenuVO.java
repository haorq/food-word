package com.meiyuan.catering.admin.vo.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.TreeNode;
import lombok.Data;

/**
 * @ClassName AdminMenuVO
 * @Description
 * @Author gz
 * @Date 2020/9/28 17:10
 * @Version 1.5.0
 */
@Data
public class AdminMenuVO extends TreeNode<AdminMenuVO> {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String label;
    private String code;
    private Long parentId;
    private Integer sort;
    private String level;

}
