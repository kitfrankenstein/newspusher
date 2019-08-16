package com.kitfrankenstein.newspusher.service.impl;

import com.kitfrankenstein.newspusher.entity.News;
import com.kitfrankenstein.newspusher.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kit
 * @date: 2019/8/13 17:28
 */
@Service
public class MqttServiceImpl implements MqttService {

    @Autowired
    private MqttPahoMessageHandler mqttMessageHandler;

    @Override
    public void publish(String topic, String content) {
        Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
        mqttMessageHandler.handleMessage(messages);
    }

    @Override
    public void publishNews(String topic, String url, String title, String digest) {
        String content = url + "\n" + title + "\n" + digest;
        Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
        mqttMessageHandler.handleMessage(messages);
    }

    @Override
    public void publishNewsList(List<News> newsList) {
        for (News news : newsList) {
            String topic = news.getTag();
            String content = news.getUrl() + "\n"
                    + news.getTitle() + "\n"
                    + news.getDigest() + "\n";
            Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
            mqttMessageHandler.handleMessage(messages);
        }
    }
}
