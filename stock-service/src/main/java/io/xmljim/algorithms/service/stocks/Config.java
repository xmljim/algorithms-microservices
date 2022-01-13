package io.xmljim.algorithms.service.stocks;

import feign.Feign;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import io.xmljim.algorithms.service.stocks.client.CPIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;


@Configuration
public class Config {

    @Bean
    @LoadBalanced
    public RestTemplate cpiRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    @LoadBalanced
    public RestTemplate statisticsRestTemplate() {
        return new RestTemplateBuilder().build();
    }
}
