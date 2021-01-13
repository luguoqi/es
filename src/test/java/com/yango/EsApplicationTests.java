package com.yango;

import com.yango.config.ElasticsearchConfig;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
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

    /**
     * 复杂查询
     *
     * 参考
     *  https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-search.html
     */
    @Test
    public void searchData() throws IOException {
//        创建检索请求
        SearchRequest searchRequest = new SearchRequest();
//        指定索引
        searchRequest.indices("bank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        指定DSL 检索条件
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

//        searchSourceBuilder.query();
//        searchSourceBuilder.from();
//        searchSourceBuilder.size();
//        searchSourceBuilder.aggregation();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
//        按照年龄值分布进行聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        searchSourceBuilder.aggregation(ageAgg);

        AvgAggregationBuilder field = AggregationBuilders.avg("balanceAvg").field("balance");
        searchSourceBuilder.aggregation(field);

        System.out.println(searchSourceBuilder.toString());
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, ElasticsearchConfig.COMMON_OPTIONS);

        System.out.println(search);
//      获取所有数据
        SearchHits hits = search.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit documentFields : hits1) {
            String sourceAsString = documentFields.getSourceAsString();
            System.out.println(sourceAsString);
        }
        Aggregations aggregations = search.getAggregations();
        Terms agg = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : agg.getBuckets()) {
            System.out.println(bucket.getKeyAsString());
        }

        Avg agg1 = aggregations.get("balanceAvg");
        System.out.println(agg1.getValue());



    }

}
