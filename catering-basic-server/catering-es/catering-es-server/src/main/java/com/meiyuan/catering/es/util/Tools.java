package com.meiyuan.catering.es.util;


import com.meiyuan.catering.es.annotation.ESID;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: esdemo
 * @description: 工具类
 * @author: X-Pacific zhang
 * @create: 2019-01-18 16:23
 **/
public class Tools {
    /**
     * 根据对象中的注解获取ID的字段值
     * @param obj
     * @return
     */
    public static String getEsId(Object obj) throws Exception {
        //getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field f : fields){
            //允许拿到 private修饰的值
            f.setAccessible(true);
            ESID esid = f.getAnnotation(ESID.class);
            if(esid != null){
                Object value = f.get(obj);
                return value.toString();
            }
        }
        return null;
    }
    /**
     * 获取o中所有的字段有值的map组合
     * @return
     */
    public static Map getFieldValue(Object o) throws IllegalAccessException {
        Map retMap = new HashMap(36);
        Field[] fs = o.getClass().getDeclaredFields();
        for(int i = 0;i < fs.length;i++){
            Field f = fs[i];
            f.setAccessible(true);
            if(f.get(o) != null){
                retMap.put(f.getName(),f.get(o) );
            }
        }
        return retMap;
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
     * GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
     */
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    public static Class getSuperClassGenricType(Class clazz, int index)
            throws IndexOutOfBoundsException {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    public static String arraytostring(String[] strs){
        if(StringUtils.isEmpty(strs)){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Arrays.asList(strs).stream().forEach(str -> sb.append(str).append(" "));
        return sb.toString();
    }

    public static boolean arrayIsNull(Object[] objs){
        if(objs == null || objs.length == 0){
            return true;
        }
        boolean flag = false;
        for (int i = 0; i < objs.length; i++) {
            if(!StringUtils.isEmpty(objs[i])){
                flag = true;
            }
        }
        if(flag){
            return false;
        }else{
            return true;
        }
    }
}
