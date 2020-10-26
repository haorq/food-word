package com.meiyuan.catering.core.storage.config;

import com.meiyuan.catering.core.storage.AliyunStorage;
import com.meiyuan.catering.core.storage.StorageService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


/**
 * @author admin
 */
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

    private final StorageProperties properties;
    private final static String ALIYUN = "aliyun";

    public StorageAutoConfiguration(StorageProperties properties) {
        this.properties = properties;
    }

    @Bean
    public StorageService storageService() {
        StorageService storageService = new StorageService();
        String active = this.properties.getActive();
        String folder = this.properties.getFolder();
        if (!StringUtils.isEmpty(active)) {
            storageService.setActive(active);
            storageService.setFolder(folder);
            if (ALIYUN.equals(active)) {
                storageService.setStorage(aliyunStorage());
            } else {
                throw new RuntimeException("当前存储模式 " + active + " 不支持");
            }
        }

        return storageService;
    }

    @Bean
    public AliyunStorage aliyunStorage() {
        AliyunStorage aliyunStorage = new AliyunStorage();
        StorageProperties.Aliyun aliyun = this.properties.getAliyun();
        if (aliyun != null) {
            aliyunStorage.setAccessKeyId(aliyun.getAccessKeyId());
            aliyunStorage.setAccessKeySecret(aliyun.getAccessKeySecret());
            aliyunStorage.setBucketName(aliyun.getBucketName());
            aliyunStorage.setEndpoint(aliyun.getEndpoint());
            aliyunStorage.setDomain(aliyun.getDomain());
        }
        return aliyunStorage;
    }


}
