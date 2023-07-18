package com.gxdxx.instagram.global.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("spring.data.redis")
public class RedisProperties {

    private String host;

    private int port;

}