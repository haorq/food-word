package com.meiyuan.catering.merchant;

import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantParamDto;
import com.meiyuan.catering.merchant.pc.MerchantPcApiApplication;
import com.meiyuan.catering.merchant.pc.service.report.MerchantPcReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MerchantPcApiApplication.class)
public class MerchantPcReportServiceTest {

    @Autowired
    private MerchantPcReportService merchantPcReportService;

    @Test
    public void test(){
        MerchantAccountDTO dto = new MerchantAccountDTO();
        MerchantParamDto param = new MerchantParamDto();
        param.setShopId(1286944790205341697L);
        param.setMerchantId(null);
        param.setType(1);
        param.setQueryType(4);
        param.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        param.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        merchantPcReportService.orderListReportMerchant(dto,param);
    }
}
