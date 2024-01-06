package com.ark.component.search.es.autoconfigure;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
public class ElasticSearchAutoConfigure {

    public ElasticSearchAutoConfigure() {
        log.info("enable [ark-component-search-es-spring-boot-starter]");
    }

    /**
     * 使用LoginUserContext填充基础字段
     */
    @Bean
    @ConditionalOnMissingBean(ElasticsearchClient.class)
    public ElasticsearchClient elasticsearchClient(ElasticsearchProperties elasticsearchProperties) {
        List<String> uris = elasticsearchProperties.getUris();
        String username = elasticsearchProperties.getUsername();
        String password = elasticsearchProperties.getPassword();
        HttpHost[] httpHosts = new HttpHost[uris.size()];
        for (int i = 0; i < httpHosts.length; i++) {
            httpHosts[i] = HttpHost.create(uris.get(i));
        }
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Math.toIntExact(elasticsearchProperties.getConnectionTimeout().toMillis()))
                .setSocketTimeout(Math.toIntExact(elasticsearchProperties.getSocketTimeout().toMillis()))
                .build();

        RestClient restClient = RestClient.builder(httpHosts)
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, credentials);
                    return httpAsyncClientBuilder
                            .setDefaultRequestConfig(requestConfig)
                            .disableAuthCaching()
                            .setDefaultCredentialsProvider(credentialsProvider);
                })
                .build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );
        return new ElasticsearchClient(transport);
    }


}
