package com.ark.component.search.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SearchTest extends ApplicationTests  {

    private final UsernamePasswordCredentials credentials
            = new UsernamePasswordCredentials("elastic", "123");
    private final HttpHost[] httpHosts = new HttpHost[1];

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

}