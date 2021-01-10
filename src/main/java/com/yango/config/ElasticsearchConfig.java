package com.yango.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.elasticsearch.client.RestClient.*;

/**
 * @Description
 * 1、导入依赖
 * 2、编写配置。给容器中注入一个 RestHighLevelClient   参考https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-getting-started-initialization.html
 *
 * @Author lugq
 * @Email lugq@yango.com.cn
 * @Date 2021/1/10 0010 23:08
 * @Version 1.0
 */
@Configuration
public class ElasticsearchConfig {

    /**
     * 如果ES有多个可以直接指定
     */
    @Bean
    public RestHighLevelClient esRestClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                builder(//new HttpHost("localhost", 9200, "http"),
                        new HttpHost("192.168.136.135", 9200, "http")));
        return client;
    }



}
