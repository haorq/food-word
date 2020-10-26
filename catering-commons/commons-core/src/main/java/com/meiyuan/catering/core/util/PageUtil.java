package com.meiyuan.catering.core.util;

/**
 * @author GongJunZheng
 * @date 2020/08/11 15:08
 * @description 简单描述
 **/

public class PageUtil {

    /**
     * 判断当前页是不是最后一页
     *
     * @param total        总页数
     * @param size         每页条数
     * @param currentPages 当前页
     * @return
     */
    public static boolean lastPages(int total, int size, int currentPages) {
        if (size == 0) {
            return true;
        }
        int pages = total / size;
        if (total % size != 0) {
            pages++;
        }
        return currentPages == pages;
    }

}
