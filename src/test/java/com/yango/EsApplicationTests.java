package com.yango;

import com.yango.config.ElasticsearchConfig;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
class EsApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void contextLoads() {
        System.out.println(client);
    }

    /**
     * 三种形式，字符串、map、对象(一般转成json)、使用source将key-vale存储
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html
     * 两种执行方式
     * 同步：Synchronous execution
     *  IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
     * 异步：Asynchronous executionedit
     *  client.indexAsync(request, RequestOptions.DEFAULT, listener);   listener：成功后调用的方法
     * 使用kibana查询posts  GET posts/_search
     */
    @Test
    public void insertData() throws IOException {
        IndexRequest request = new IndexRequest("posts");
        request.id("1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);//注意json时要传内容类型
//        执行操作并响应数据
        IndexResponse indexResponse = client.index(request, ElasticsearchConfig.COMMON_OPTIONS);
        System.out.println(indexResponse);
    }

}
