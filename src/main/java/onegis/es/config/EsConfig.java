package onegis.es.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Es配置类，提供client连接
 *
 * @author yz
 * @date 2018/12/06
 */
@Configuration
public class EsConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private Integer port;

    @Value("${elasticsearch.schema}")
    private String schema;

    @Value("${elasticsearch.connectionRequestTimeOut}")
    private int connectTimeOut;

    @Value("${elasticsearch.socketTimeOut}")
    private int socketTimeOut;

    @Value("${elasticsearch.connectionRequestTimeOut}")
    private int connectionRequestTimeOut;

    @Value("${elasticsearch.maxConnectNum}")
    private int maxConnectNum;

    @Value("${elasticsearch.maxConnectPerRoute}")
    private int maxConnectPerRoute;

    @Value("${elasticsearch.uniqueConnectTimeConfig}")
    private boolean uniqueConnectTimeConfig;

    @Value("${elasticsearch.uniqueConnectNumConfig}")
    private boolean uniqueConnectNumConfig;

    private RestClientBuilder builder;
    private RestHighLevelClient client;

    /**
     * 注入client连接Bean
     *
     * @return transportClient
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        HttpHost httpHost = new HttpHost(host, port, schema);
        builder = RestClient.builder(httpHost);
        if (uniqueConnectTimeConfig) {
            setConnectTimeOutConfig();
        }
        if (uniqueConnectNumConfig) {
            setMutiConnectConfig();
        }
        client = new RestHighLevelClient(builder);
        return client;
    }

    /**
     * 主要关于异步httpclient的连接延时配置
     */
    public void setConnectTimeOutConfig() {
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectTimeout(connectTimeOut);
                requestConfigBuilder.setSocketTimeout(socketTimeOut);
                requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
                return requestConfigBuilder;
            }
        });
    }

    /**
     * 主要关于异步httpclient的连接数配置
     */
    public void setMutiConnectConfig() {
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.setMaxConnTotal(maxConnectNum);
                httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
                return httpClientBuilder;
            }
        });
    }
}
