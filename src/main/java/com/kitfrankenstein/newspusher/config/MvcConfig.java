package com.kitfrankenstein.newspusher.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.kitfrankenstein.newspusher.component.LoginInterceptor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kit
 * @date: 2019/8/1 18:27
 */
@EnableConfigurationProperties(MqttProperties.class)
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private MqttProperties mqttProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/view/login").setViewName("login");
        registry.addViewController("/view/register").setViewName("register");
        registry.addViewController("/view/console").setViewName("console");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(){
        //1.需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //2:添加fastJson的配置信息;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        /*
          SerializerFeature.WriteMapNullValue 指定当属性值为null是是否输出：pro:null
          SerializerFeature.DisableCircularReferenceDetect 关闭循环引用检测
         */
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect);
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        //4.在converter中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters((HttpMessageConverter<?>) fastJsonHttpMessageConverter);
    }

    @Bean
    public MqttPahoMessageHandler mqttMessageHandler() {
        //连接选项
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword());
        options.setCleanSession(mqttProperties.getCleanSession());
        options.setKeepAliveInterval(mqttProperties.getKeepAlive());
        options.setServerURIs(mqttProperties.getHost());
        options.setMaxInflight(mqttProperties.getMaxInflight());

        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setConnectionOptions(options);
        //使用MqttPahoMessageHandler发布消息
        MqttPahoMessageHandler mqttPahoMessageHandler = new MqttPahoMessageHandler(mqttProperties.getClientId(), clientFactory);
        mqttPahoMessageHandler.setAsync(mqttProperties.getAsync());
        mqttPahoMessageHandler.setDefaultQos(mqttProperties.getQos());
        mqttPahoMessageHandler.setCompletionTimeout(mqttProperties.getTimeout());
        return mqttPahoMessageHandler;
    }
}
