package com.ark.component.search.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class IndexTest extends ApplicationTests  {

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

    @Test
    public void testCreateIndex() {
        RestHighLevelClient client = getClient();
        try {
            IndicesClient indices = client.indices();
            if (indices.exists(new GetIndexRequest("hotel"), RequestOptions.DEFAULT)) {
                indices.delete(new DeleteIndexRequest("hotel"), RequestOptions.DEFAULT);
                System.out.println("索引已存在，先删除索引");
            }
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("hotel")
                    .source(MAPPINGS, XContentType.JSON);
            indices.create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testDeleteIndex() {
        RestHighLevelClient client = getClient();
        try {
            IndicesClient indices = client.indices();
            if (indices.exists(new GetIndexRequest("hotel"), RequestOptions.DEFAULT)) {
                indices.delete(new DeleteIndexRequest("hotel"), RequestOptions.DEFAULT);
                System.out.println("索引已存在，先删除索引");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RestHighLevelClient getClient() {
        httpHosts[0] = new HttpHost("localhost", 9200, "http");
        RestClientBuilder builder = RestClient.builder(httpHosts)
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    httpAsyncClientBuilder.disableAuthCaching();
                    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, credentials);
                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                });
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }

}