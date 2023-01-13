package com.ark.component.search.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SearchTest extends ApplicationTests  {

    private final UsernamePasswordCredentials credentials
            = new UsernamePasswordCredentials("elastic", "123");
    private HttpHost[] httpHosts = new HttpHost[1];

    @Test
    public void testSearchMatchAllQuery() {
        RestHighLevelClient client = getClient();
        try {
            SearchRequest searchRequest = new SearchRequest("hotel");
            searchRequest.source().query(QueryBuilders.matchAllQuery());
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            for (SearchHit hit : search.getHits().getHits()) {
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSearchMatchQuery() {
        RestHighLevelClient client = getClient();
        try {
            SearchRequest searchRequest = new SearchRequest("hotel");
            MatchQueryBuilder query = QueryBuilders.matchQuery("all", "越秀");
            SearchSourceBuilder source = searchRequest.source();
            source.query(query);
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = search.getHits();
            for (SearchHit hit : hits.getHits()) {
                System.out.println(hit.getSourceAsString());
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
        return new RestHighLevelClient(builder);
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5044);
            socket.getOutputStream().write("测试一下".getBytes(StandardCharsets.UTF_8));
//            socket.getOutputStream().write("14.49.42.25 - - [12/May/2019:01:24:44 +0000] \"GET /articles/ppp-over-ssh/ HTTP/1.1\" 200 18586 \"-\" \"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2b1) Gecko/20091014 Firefox/3.6b1 GTB5\"\n".getBytes(StandardCharsets.UTF_8));
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        }
    }
}