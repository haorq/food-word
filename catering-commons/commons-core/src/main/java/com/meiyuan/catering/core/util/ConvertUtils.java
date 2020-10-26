package com.meiyuan.catering.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 数据对象转换工具
 * @author admin
 */
public class ConvertUtils {

    private static Logger logger = LoggerFactory.getLogger(ConvertUtils.class);

    /**
     *  转化出新对象
     * @param source 源对象
     * @param target 目标对象
     * @param ignoreProperties 忽略属性
     * @param <T>
     * @return
     */
    public static <T> T sourceToTarget(Object source, Class<T> target,String... ignoreProperties){
        if(source == null){
            return null;
        }
        T targetObject = null;
        try {
            targetObject = target.newInstance();
            BeanUtils.copyProperties(source, targetObject,ignoreProperties);
        } catch (Exception e) {
            logger.error("convert error ", e);
        }
        return targetObject;
    }



    /**
     * 转化出新对象集合
     * @param sourceList 源对象集合
     * @param target 目标对象
     * @param <T> 新对象集合
     * @return
     */
    public static <T> List<T> sourceToTarget(Collection<?> sourceList, Class<T> target){
        if(sourceList == null){
            return null;
        }
        List targetList = new ArrayList<>(sourceList.size());
        try {
            for(Object source : sourceList){
                T targetObject = target.newInstance();
                BeanUtils.copyProperties(source, targetObject);
                targetList.add(targetObject);
            }
        }catch (Exception e){
            logger.error("convert error ", e);
        }

        return targetList;
    }
}
