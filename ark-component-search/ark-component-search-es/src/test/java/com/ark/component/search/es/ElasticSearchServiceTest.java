package com.ark.component.search.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class ElasticSearchServiceTest extends ApplicationTests  {

    private final UsernamePasswordCredentials credentials
            = new UsernamePasswordCredentials("elastic", "407Xj7+o1jU99EB9Qy32");
    private HttpHost[] httpHosts = new HttpHost[1];

    @Test
    public void test() {
        httpHosts[0] = new HttpHost("localhost", 9200, "http");
        RestClientBuilder builder = RestClient.builder(httpHosts)
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    httpAsyncClientBuilder.disableAuthCaching();
                    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, credentials);
                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                });
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        try {
            GetResponse hotel = restHighLevelClient.get(new GetRequest("hotel", "001"), RequestOptions.DEFAULT);
            Map<String, Object> source = hotel.getSource();
            System.out.println(source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}