package com.meiyuan.catering.es.config;

import com.meiyuan.catering.es.util.Constant;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

/**
 * @program: esdemo
 * @description: 自动配置注入restHighLevelClient
 * @author: X-Pacific zhang
 * @create: 2019-01-07 14:09
 **/
@Configuration
public class ElasticSearchConfiguration  {

    @Value("${spring.elasticsearch.jest.proxy.host}")
    private String host;
    @Value("${aliEs.name}")
    private String name;
    @Value("${aliEs.password}")
    private String password;
    @Value("${aliEs.flag}")
    private boolean flag;
    private RestHighLevelClient restHighLevelClient;

    /**
     * 这个close是调用RestHighLevelClient中的close
     */
    @Bean(destroyMethod="close")
    @Scope("singleton")
    public RestHighLevelClient createInstance() {
        try {
            if(StringUtils.isEmpty(host)){
                host = Constant.DEFAULT_ES_HOST;
            }
            String[] hosts = host.split(",");
            HttpHost[] httpHosts = new HttpHost[hosts.length];
            for (int i = 0; i < httpHosts.length; i++) {
                String h = hosts[i];
                httpHosts[i] = new HttpHost(h.split(":")[0], Integer.parseInt(h.split(":")[1]), "http");
            }
            RestClientBuilder builder = RestClient.builder(httpHosts);
            if (flag) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(name, password));
                builder.setHttpClientConfigCallback(
                        httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                );
            }
            restHighLevelClient = new RestHighLevelClient(builder);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return restHighLevelClient;
    }
}
