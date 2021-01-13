package com.yango.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.elasticsearch.client.RestClient.*;

/**
 * @Description

 * @Author lugq
 * @Email lugq@yango.com.cn
 * @Date 2021/1/10 0010 23:08
 * @Version 1.0
 */
@Configuration
public class ElasticsearchConfig {

    /**
     * 如果ES有多个可以直接指定
     * 1、导入依赖
     * 2、编写配置。给容器中注入一个 RestHighLevelClient
     *      参考 https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-getting-started-initialization.html
     */
    @Bean
    public RestHighLevelClient esRestClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                builder(//new HttpHost("localhost", 9200, "http"),
                        new HttpHost("192.168.136.135", 9200, "http")));
        return client;
    }

    /**
     * RequestOptions 对所有请求进行统一设置，官网建议做成单实例，供所有请求使用
     * 请求的通用设置项(例如ES添加了安全访问规则，则可以设置安全的令牌信息)
     * 参考：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-low-usage-requests.html#java-rest-low-usage-request-options
     * 然后测试ES的增删改查
     */
    public static final RequestOptions COMMON_OPTIONS;//通用设置项
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);//每次请求之前带上请求头信息，比如令牌
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024)
//        );//异步相关，定义响应消费者
        COMMON_OPTIONS = builder.build();
    }



}
