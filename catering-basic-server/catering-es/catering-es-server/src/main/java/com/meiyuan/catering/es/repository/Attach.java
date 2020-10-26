package com.meiyuan.catering.es.repository;

import lombok.Data;

/**
 * @program: esclientrhl
 * @description: 提供更强的查询功能可选条件
 * @author: X-Pacific zhang
 * @create: 2019-10-14 10:42
 **/
@Data
public class Attach {
    private String[] includes;
    private String[] excludes;

    public void addIncludes(String... includes) {
        this.includes = includes;
    }

    public void addExcludes(String... excludes) {
        this.excludes = excludes;
    }

    public Attach() {
    }

    public Attach(String... includes) {
        this.includes = includes;
    }
}
