package com.mysql.to.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
@Slf4j
class DataSynchronizationApplicationTests {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    //创建索引 不指定 mapping
    @Test
    void createIndex() throws IOException {
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices()
                .create(createIndexRequest -> createIndexRequest.index("my_index2"));
        log.info("索引是否创建成功：" + createIndexResponse.acknowledged());
    }

    //创建索引 指定 mapping
    @Test
    void createIndexMapping() throws IOException {
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices()
                .create(createIndexRequest -> createIndexRequest.index("my_index2")
                        // 用 lambda 的方式 下面的 mapping 会覆盖上面的 mapping
                        .mappings(typeMapping -> typeMapping
                                .properties("name",pr -> pr.text( text -> text.fielddata(true)))
                                .properties("age", pr -> pr.integer(integer -> integer.index(true)))
                        )
                );
        log.info("索引是否创建成功：" + createIndexResponse.acknowledged());
    }

    //判断索引是否存在
    @Test
    void indexIsExist() throws IOException {
        BooleanResponse booleanResponse = elasticsearchClient.indices()
                .exists(existsRequest -> existsRequest.index("my_index2"));
        log.info("索引是否存在：" + booleanResponse.value());
    }

    //查看索引的详细信息
    @Test
    void catIndexDetail() throws IOException {
        GetIndexResponse getIndexResponse = elasticsearchClient.indices()
                .get(getIndexRequest -> getIndexRequest.index("my_index2"));
        Map<String, Property>  map = getIndexResponse.get("my_index2").mappings().properties();

        for (String key : map.keySet()) {
            log.info("索引的字段：" + map.get(key)._kind());
        }
    }

    //删除索引
    @Test
    void deleteIndex() throws IOException {
        DeleteIndexResponse d = elasticsearchClient.indices()
                .delete(deleteIndexRequest -> deleteIndexRequest.index("my_index2"));
        log.info("索引是否删除成功：" + d.acknowledged());

    }

    //简单 search
    @Test
    void searchTest() throws IOException {
        SearchResponse<SearchData> search = elasticsearchClient.search(s -> s.index("my_index")
                        .query(q -> q
                                .match(m -> m
                                        .field("name")
                                        .query("lixuanfeng")))
                , SearchData.class);

        for (Hit<SearchData> hit:search.hits().hits()) {
            log.info("hit: {}", hit.source());
        }
    }

    //查询单个文档
    @Test
    void contextLoads() throws IOException {

         //查询单个文档
        GetResponse<SearchData> getResponse = elasticsearchClient.get(getRequest ->
                getRequest
                        .index("my_index")
                        .id("6"), SearchData.class);
        log.info("获取到的文档：" +  getResponse.source());

    }

    // 根据id更新数据
    @Test
    void updateSearch () throws IOException {
        UpdateResponse<SearchData> updateResponse = elasticsearchClient.update(updateRequest ->
                updateRequest
                        .index("my_index")
                        .id("7")
                        .doc( new SearchData(60,"cvb@qq.com","jingweiwei","beijing pingguoyuan", 200.69)),
                SearchData.class);
        log.info("获取到响应：" +  updateResponse.result());
    }

    //根据id 删除文档信息
    @Test
    void deleteDoc() throws IOException {
//        DeleteResponse deleteResponse = elasticsearchClient.delete(deleteDocRequest ->
//                deleteDocRequest.index("my_index").id("1,2"));
        DeleteByQueryResponse age = elasticsearchClient.deleteByQuery(d -> d
                .index("my_index").query(q -> q
                        .range(r -> r
                                .field("age")
                                .lte(JsonData.of(10)))));
        log.info("是否删除成功：" +  age.deleted());

    }

    //批量插入数据
    @Test
    void insertBulkDoc() throws IOException {
        List<BulkOperation> bulkOperations = new ArrayList<>();

        for (int i=0; i < 10; i++) {
            SearchData searchData = new SearchData();
            searchData.setAge(i + 1);
            searchData.setEmail(i + "abc@qq.com");
            searchData.setName(i + "test");
            searchData.setAddress("beijng 2" + i);
            searchData.setBalance(Double.parseDouble( i+""));
            bulkOperations.add(
                    new BulkOperation.Builder()
                            .create(c -> c
                                    .document(searchData)).build());
        }
        BulkResponse b = elasticsearchClient.bulk(bulkRequest -> bulkRequest.index("my_index").operations(bulkOperations));

        // false 代表没有异常插入成功
        log.info("批量添加数据是否成功：" +  b.errors());
    }


    //查询索引里的全部数据
    @Test
    void searchAllTest() throws IOException {
        SearchResponse<SearchData> search = elasticsearchClient.search(s -> s.index("my_index")
                        .query(q -> q.matchAll( mall -> mall))
                        .from(0)
                        .size(100)
                , SearchData.class);

        for (Hit<SearchData> hit:search.hits().hits()) {
            log.info("hit: {}", hit.source());
        }
    }

    //根据 name 查询相应的文档，
    @Test
    void search01() throws IOException {
        SearchResponse<SearchData> searchDataSearchResponse = elasticsearchClient.search(searchRequest -> searchRequest
                .index("my_index")
                .query(q -> q.term(t -> t.field("name").value(v -> v.stringValue("lixuanfeng"))))
                ,SearchData.class);

        for (Hit<SearchData> hit: searchDataSearchResponse.hits().hits()) {
            log.info("hit: source: {}, id: {}", hit.source(), hit.id());
        }
    }

    //#bool用来做复合查询
    @Test
    void boolSearch() throws IOException {
        SearchRequest searchRequest = SearchRequest.of(searchRequests -> searchRequests.index("my_index")
                // 如果有多个 .query 后面的 query 会覆盖前面的 query
                .query(q -> q
                        .bool(b -> b
                                .must(mu -> {
                                        mu.term(ma -> ma
                                            .field("address").value("zhuminghao"));
                                        mu.term(ma -> ma
                                            .field("email").value("aaa@qq.com"));
                                        return mu;
                                      }
                                )
                                .mustNot(mn -> mn.term(ter -> ter.field("age").value("47")))
                        )));

        SearchRequest request = SearchRequest.of(searchRequest1 ->
                searchRequest1.index("my_index").from(0).size(20).sort(s -> s.field(f -> f.field("age").order(SortOrder.Desc)))
                        .query(query ->
                                query.bool(boolQuery ->
                                        boolQuery
                                                // 两个 should 连用是没有问题的
                                                .should(must -> must.term(
                                                        e -> e.field("age").value(value -> value.stringValue("59"))
                                                ))
                                                .should(must -> must.term(
                                                        e -> e.field("age").value(value -> value.stringValue("47"))
                                                ))
                                )
                        ));


        SearchResponse<SearchData> searchResponse = elasticsearchClient.search(searchRequest,SearchData.class);
        SearchResponse<SearchData> searchResponse1 = elasticsearchClient.search(request,SearchData.class);

        for (Hit<SearchData> hit: searchResponse.hits().hits()) {
            log.info("searchResponse hit: source: {}, id: {}", hit.source(), hit.id());
        }

        for (Hit<SearchData> hit: searchResponse1.hits().hits()) {
            log.info("searchResponse1 hit: source: {}, id: {}", hit.source(), hit.id());
        }
    }

    //按照年龄聚合，并且求这些年龄段的这些人的平均薪资
    @Test
    void searchAvg() throws IOException {
        SearchRequest request = SearchRequest.of(searchRequest -> searchRequest
                .index("my_index")
                .aggregations("ageAgg", t -> t
                        .terms(f -> f
                                .field("age").size(10))
                        .aggregations("avgAgg", avg -> avg.avg(a -> a.field("balance"))
                )));
        Map<String, Aggregate> aggregations = elasticsearchClient.search(request, SearchData.class).aggregations();
        for (Map.Entry<String, Aggregate> ee : aggregations.entrySet()) {
            Aggregate value = ee.getValue();
            LongTermsAggregate longTermsAggregate = (LongTermsAggregate) value._get();
            Buckets<LongTermsBucket> buckets = longTermsAggregate.buckets();
            for (LongTermsBucket bucket : buckets.array()){
                log.info(" 年龄阶段: {} - 此年龄阶段共多少人：{}", bucket.key(), bucket.docCount());
                Map<String, Aggregate> avgAgg = bucket.aggregations();
                for(String avggg : avgAgg.keySet()) {
                    AvgAggregate avgAggregate = (AvgAggregate) avgAgg.get(avggg)._get();
                    log.info("每个年龄阶段的平均工资 : {}", avgAggregate.value());
                }
            }

        }


    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class SearchData{
        private Integer age;
        private String email;
        private String name;
        private String address;
        private Double balance;
    }

    @Test
    void testo1() {
        //通过构造引用将所有东西粘合在一起：
        PersonFactory<Person> personPersonFactory = Person::new;
        //personPersonFactory.create("喧锋", "李");

        //Accessing local variables(访问本地变量)
        final int num = 1;
        Converter<Integer, String> stringConverter = (from) -> String.valueOf(from + num);
        log.info(stringConverter.converter(2));
    }

    @Test
    void strToDate() {
        String timeStr = "2019-08-26 18:03:33";
        DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(timeStr, timeDtf);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        log.info("date: {}", date.toString());
    }

}
class Person {
    String firstName;
    String lastName;

    Person() {
        System.out.print("哈哈哈");
    }

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

        System.out.print(firstName + " : " + lastName);
    }
}
interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
interface Converter<F,T> {
    T converter(F from);
}