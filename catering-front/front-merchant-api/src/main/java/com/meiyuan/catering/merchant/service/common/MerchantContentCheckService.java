package com.meiyuan.catering.merchant.service.common;

import com.meiyuan.catering.core.check.ContentCheckService;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author MeiTao
 * @Description 文本/图片 类容审核service
 * @Date  2020/3/28 0028 9:41
 */
@Service
public class MerchantContentCheckService {
    @Autowired
    private ContentCheckService contentCheckService;

    /**
     * 图片审查--图片
     * @param file
     * @return
     */
    public Result checkImageByFile(MultipartFile file) {
        return contentCheckService.checkImageByFile(file);
    }

    /**
     * 图片审查 -- 图片地址
     * @param url 图片地址
     * @return
     */
    public Result checkImageByUrl(String url) {
        return contentCheckService.checkImageByUrl(url);
    }

    /**
     * 文本内容审核
     * @param text 待审核文本内容
     * @return
     */
    public Result checkText(String text) {
        return contentCheckService.checkText(text);
    }
}
