package com.meiyuan.catering.merchant.pc.service.storage;

import com.google.common.collect.Lists;
import com.meiyuan.catering.core.check.ContentCheckService;
import com.meiyuan.catering.core.storage.StorageService;
import com.meiyuan.catering.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName MerchantPcStorageService
 * @Description
 * @Author gz
 * @Date 2020/6/30 9:48
 * @Version 1.1
 */
@Service
public class MerchantPcStorageService {
    @Resource
    private StorageService storageService;

    @Autowired
    private ContentCheckService contentCheckService;

    /**
     * 单文件上传
     * @param file
     * @return
     * @throws IOException
     */
    public Result create(MultipartFile file) throws IOException {
        //图片校验
        //contentCheckService.checkImageByFile(file);
        String originalFilename = file.getOriginalFilename();
        String url = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(),
                originalFilename);
        return Result.succ(url);
    }

    /**
     * 批量上传
     * @param files
     * @return
     * @throws IOException
     */
    @SuppressWarnings("all")
    public Result createBatch(MultipartFile[] files) throws IOException {
        if(files==null || files.length==0){
            return Result.badArgument();
        }
        List<String> urls = Lists.newArrayList();
        for (MultipartFile file : files) {
            //图片校验
            //contentCheckService.checkImageByFile(file);
            String originalFilename = file.getOriginalFilename();
            String url = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(),
                    originalFilename);
            urls.add(url);
        }
        return Result.succ(urls);
    }
}
