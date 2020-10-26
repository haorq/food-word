package com.meiyuan.catering.core.storage;

import com.meiyuan.catering.core.util.CharUtil;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 提供存储服务类，所有存储服务均由该类对外提供
 *
 * @author admin
 */
public class StorageService {
    private String active;
    private Storage storage;
    private String folder;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * 存储一个文件对象
     *
     * @param inputStream   文件输入流
     * @param contentLength 文件长度
     * @param contentType   文件类型
     * @param fileName      文件索引名
     */
    public String store(InputStream inputStream, long contentLength, String contentType, String fileName) {
        String key = generateKey(fileName);
        storage.store(inputStream, contentLength, contentType, key);
        return generateUrl(key);
    }

    private String generateKey(String originalFilename) {
        String suffix = "."+originalFilename;
        int index = originalFilename.lastIndexOf('.');
        if (index != -1) {
            suffix = originalFilename.substring(index);
        }
        String prefix = folder + "/" + DateUtils.formatDate(new Date(), "yyyyMMdd");
        return prefix + "/" + CharUtil.getRandomString(20) + suffix;
    }

    public Stream<Path> loadAll() {
        return storage.loadAll();
    }

    public Path load(String keyName) {
        return storage.load(keyName);
    }

    public Resource loadAsResource(String keyName) {
        return storage.loadAsResource(keyName);
    }

    public void delete(String keyName) {
        storage.delete(keyName);
    }

    private String generateUrl(String keyName) {
        return storage.generateUrl(keyName);
    }

    public Map<String, String> aliyunOssSignature() {
        Map<String, String> map = storage.aliyunOssSignature();
        map.put("folder", folder + "/" + DateUtils.formatDate(new Date(), "yyyyMMdd"));
        return map;
    }
}
