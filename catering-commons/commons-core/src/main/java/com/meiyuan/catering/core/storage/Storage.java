package com.meiyuan.catering.core.storage;

import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 对象存储接口
 * @author admin
 */
public interface Storage {

	/**
	 * 存储一个文件对象
	 *
	 * @param inputStream
	 *            文件输入流
	 * @param contentLength
	 *            文件长度
	 * @param contentType
	 *            文件类型
	 * @param keyName
	 *            文件名
	 */
	void store(InputStream inputStream, long contentLength, String contentType, String keyName);
	/**
	 * 方法描述: <br>
	 *
	 * @author: gz
	 * @date: 2020/6/23 15:51
	 * @param
	 * @return: {@link Stream< Path>}
	 * @version 1.1.1
	 **/
	Stream<Path> loadAll();
	/**
	 * 方法描述: <br>
	 *
	 * @author: gz
	 * @date: 2020/6/23 15:50
	 * @param keyName
	 * @return: {@link Path}
	 * @version 1.1.1
	 **/
	Path load(String keyName);
	/**
	 * 方法描述: 加载资源<br>
	 *
	 * @author: gz
	 * @date: 2020/6/23 15:50
	 * @param keyName
	 * @return: {@link Resource}
	 * @version 1.1.1
	 **/
	Resource loadAsResource(String keyName);
	/**
	 * 方法描述: 删除<br>
	 *
	 * @author: gz
	 * @date: 2020/6/23 15:49
	 * @param keyName
	 * @return: {@link }
	 * @version 1.1.1
	 **/
	void delete(String keyName);
	/**
	 * 方法描述: 生成url<br>
	 *
	 * @author: gz
	 * @date: 2020/6/23 15:49
	 * @param keyName
	 * @return: {@link }
	 * @version 1.1.1
	 **/
	String generateUrl(String keyName);

	/**
	 * 阿里云OSS签名，供前端直接跟OSS对接
	 *
	 * @return
	 */
	Map<String, String> aliyunOssSignature();
}
