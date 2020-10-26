package com.meiyuan.catering.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：切割list
 *
 * @author fql
 */
public class SplitListUtil {
    public static <T> List<List<T>> splitList(List<T> list, int groupSize){
        int length = list.size();
        // 计算可以分成多少组
        int num = ( length + groupSize - 1 )/groupSize;
        List<List<T>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = Math.min((i + 1) * groupSize, length);
            newList.add(list.subList(fromIndex,toIndex)) ;
        }
        return  newList ;
    }
}
