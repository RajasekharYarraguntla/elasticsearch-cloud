package com.raja.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.rest.uris}")
    private String elasticsearchHost;
    @Value("${spring.elasticsearch.rest.username}")
    private String username;
    @Value("${spring.elasticsearch.rest.password}")
    private String password;

    @Bean
    public co.elastic.clients.elasticsearch.ElasticsearchClient saveDocument() {
        RestClientBuilder builder = RestClient.builder(
                        new HttpHost(elasticsearchHost, 9243, "https"))
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(5000).setSocketTimeout(60000));

        String userNameAndPassword = username + ":" + password;
        Header[] defaultHeaders = new Header[]{new BasicHeader("Authorization", "Basic "
                + java.util.Base64.getEncoder().encodeToString(userNameAndPassword.getBytes()))};
        builder.setDefaultHeaders(defaultHeaders);
        RestClient restClient = builder.build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}

