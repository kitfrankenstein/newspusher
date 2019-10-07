package com.kitfrankenstein.newspusher.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kit
 * @date: 2019/10/7 23:09
 */
@ConfigurationProperties(prefix = "mqtt")
@Data
public class MqttProperties {

    private String[] host;
    private String clientId;
    private String username;
    private char[] password;
    private Boolean cleanSession;
    private Integer keepAlive;
    private Integer maxInflight;
    private Boolean async;
    private Integer qos;
    private Integer timeout;

}
