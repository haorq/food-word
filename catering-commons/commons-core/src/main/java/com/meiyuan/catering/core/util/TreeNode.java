package com.meiyuan.catering.core.util;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TreeNode
 * @Description 树节点，所有需要实现树节点的，都需要继承该类
 * @Author gz
 * @Date 2020/9/28 17:10
 * @Version 1.5.0
 */
@Data
public class TreeNode<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 上级ID
     */
    private Long parentId;
    /**
     * 子节点列表
     */
    private List<T> children = new ArrayList<>();

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
