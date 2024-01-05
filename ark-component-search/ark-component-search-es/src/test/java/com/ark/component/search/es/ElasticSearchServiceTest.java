package com.ark.component.search.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.SourceField;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.ark.component.search.es.repository.Hotel;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

public class ElasticSearchServiceTest extends ApplicationTests {

    private final UsernamePasswordCredentials credentials
            = new UsernamePasswordCredentials("elastic", "123");
    private HttpHost[] httpHosts = new HttpHost[1];

    private String MAPPINGS = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"address\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"price\": {\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"score\": {\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"brand\": {\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"city\": {\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"starName\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"business\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"location\": {\n" +
            "        \"type\": \"geo_point\"\n" +
            "      },\n" +
            "      \"pic\": {\n" +
            "        \"type\": \"keyword\", \n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"all\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private ElasticsearchClient client;

    @BeforeEach
    public void beforeAll() {
        client = getClient();
    }

    /**
     * 创建索引
     */
    @Test
    public void testCreateIndex() {
        try {
            CreateIndexResponse response = client
                    .indices()
                    .create((builder -> builder.index("hotel")));
            System.out.println(response.acknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateIndexWithMappings() {
        ElasticsearchClient client = getClient();
        try {
            CreateIndexResponse response = client.indices().create((builder -> builder.index("hotel").mappings(fn -> fn.source(SourceField.of(s -> s.withJson(new StringReader(MAPPINGS)))))));
            System.out.println(response.acknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeleteIndex() {
        ElasticsearchClient client = getClient();
        try {

            ElasticsearchIndicesClient indices = client.indices();
            if (indices.exists(fn -> fn.index("hotel")).value()) {
                DeleteIndexResponse response = indices.delete(builder -> builder.index("hotel"));
                System.out.println(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateDoc() {
        ElasticsearchClient client = getClient();
        try {
            IndexRequest hotel = new IndexRequest("hotel");
            hotel.id("2");
            hotel.source("{\n" +
                    "  \"brand\": \"维也纳酒店\",\n" +
                    "  \"name\": \"广州小酒店\",\n" +
                    "  \"price\": \"100\",\n" +
                    "  \"city\": \"广州\"\n" +
                    "}", XContentType.JSON);
            System.out.println(client.index(fn -> fn.index("hotel").document(hotel)).result().jsonValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetDocById() {
        ElasticsearchClient client = getClient();
        try {
            co.elastic.clients.elasticsearch.core.GetResponse<Hotel> hotel = client.get(fn -> fn.index("hotel").id("2"), Hotel.class);
            System.out.println(hotel.source());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
//    public void testUpdateDocById() {
//        ElasticsearchClient client = getClient();
//        try {
//            UpdateRequest request = new UpdateRequest("hotel", "2");
//            request.doc("name", "汉庭");
//            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
//            System.out.println(response);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    public void testDeleteDocById() {
//        ElasticsearchClient client = getClient();
//        try {
//            DeleteRequest request = new DeleteRequest("hotel", "2");
//            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
//            System.out.println(response);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    public void test() {
//        ElasticsearchClient restHighLevelClient = getClient();
//        try {
//            GetResponse hotel = restHighLevelClient.get(new GetRequest("hotel", "1"), RequestOptions.DEFAULT);
//            Map<String, Object> source = hotel.getSource();
//            System.out.println(source);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                restHighLevelClient.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    private ElasticsearchClient getClient() {
        httpHosts[0] = new HttpHost("localhost", 9200, "http");
        RestClientBuilder builder = RestClient.builder(httpHosts)
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, credentials);
                    return httpAsyncClientBuilder
                            .setDefaultRequestConfig(RequestConfig.custom().build())
                            .disableAuthCaching()
                            .setDefaultCredentialsProvider(credentialsProvider);
                });
        RestClient restClient = builder.build();


        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }

}