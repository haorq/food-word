package com.meiyuan.catering.admin;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.meiyuan.catering.admin.entity.CateringRegionEntity;
import com.meiyuan.catering.admin.service.CateringRegionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaoozu
 * @description 添加百度行政区域表数据
 * @date 2020/3/1710:43
 * @since v1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRegion {

   private List<CateringRegionEntity> list = new ArrayList<>();
   @Autowired
   private CateringRegionService regionService;

    @Test
    public void importData(){
        String fileName = TestRegion.getPath() + "demo" + File.separator + "region.xlsx";
        EasyExcel.read(fileName,CateringRegionDTO.class,new TestRegionHeadDataListener()).sheet().doRead();
    }

    public static String getPath() {
        return TestRegion.class.getResource("/").getPath();
    }

    class TestRegionHeadDataListener extends AnalysisEventListener<CateringRegionDTO>{
        private Map<String,String> map = new HashMap<>(1024);

        @Override
        public void invoke(CateringRegionDTO data, AnalysisContext context) {
            String key = data.getProvinceCode()+"-"+data.getCityCode()+"-"+data.getDistrictCode();
            if (!map.containsKey(key)){
                map.put(key,key);
                CateringRegionEntity entity = new CateringRegionEntity();
                BeanUtils.copyProperties(data,entity);
                list.add(entity);
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            regionService.batchSave(list);
        }
    }
}

