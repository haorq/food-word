package com.meiyuan.catering.core.qrcode;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author lhm
 * @date 2020/5/7 16:38
 **/
@Component
public class QrCodeUtils {

    @Resource
    private WxMaService wxMaService;

    @Value("${createCode.enable}")
    private String enable;

    @Resource
    private StorageService storageService;

    /**
     * 创建小程序图
     *
     * @param id
     * @return
     */
    public String createPusherImage(Long id) {
        FileInputStream inputStream = null;
        try {
            File file;
            // 创建该商品的二维码
            System.out.println(enable);
            if (Boolean.TRUE.toString().equals(enable)) {
                file = wxMaService.getQrcodeService().createWxaCodeUnlimit(id.toString(), "sspages/scanCode/index");
            } else {
                file = wxMaService.getQrcodeService().createWxaCode("groundPusherId=" + id);
            }
            inputStream = new FileInputStream(file);
            // 存储分享图
            String url = storageService.store(inputStream, file.length(), "image/jpeg", id.toString()
            );
            return url;
        } catch (Exception e) {
            throw new CustomException("创建二维码失败");
        } finally {
            try{
                if(inputStream!=null){
                    inputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

}
