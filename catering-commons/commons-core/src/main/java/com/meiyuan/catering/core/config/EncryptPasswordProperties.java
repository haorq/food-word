package com.meiyuan.catering.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author MeiTao
 * @Description 密码加密key
 * @Date  2020/3/26 0026 18:41
 */
@Configuration
@ConfigurationProperties(prefix = "psssword.encrypt")
@Data
public class EncryptPasswordProperties {
	private String key;

	private String iv;

}
