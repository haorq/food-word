package com.meiyuan.catering.core.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.StringUtils;
import com.meiyuan.catering.core.redis.utils.DicUtils;
import com.meiyuan.catering.core.util.SpringContextUtils;

import java.util.Map;

/**
 * Converter excel dict's key to value
 * @author admin
 */
public class ExcelDictConverter implements Converter<Integer> {

    private static final String VAR_PAY_WAY = "payWay";
    @Override
    public Class supportJavaTypeKey() {
        return Long.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return cellData.getNumberValue().intValue();
    }

    @Override
    public CellData convertToExcelData(Integer integer, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String dictType = excelContentProperty.getNumberFormatProperty().getFormat();
        if (StringUtils.isEmpty(dictType)){
            return new CellData(integer.toString());
        }
        if (VAR_PAY_WAY.equals(dictType) && integer == 0){
            return new CellData("-");
        }
        DicUtils dictRedis = SpringContextUtils.getBean(DicUtils.class);
        Map<Integer,String> map = dictRedis.getNames(dictType);
        if (map.isEmpty()){return new CellData(integer.toString());}
        String value = map.get(integer);
        if (value == null){return new CellData(integer.toString());}
        return new CellData(value);
    }
}
