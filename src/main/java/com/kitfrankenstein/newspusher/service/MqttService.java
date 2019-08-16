package com.kitfrankenstein.newspusher.service;

import com.kitfrankenstein.newspusher.entity.News;

import java.util.List;

/**
 * @author Kit
 * @date: 2019/8/12 15:07
 */
public interface MqttService {

    void publish(String topic, String content);

    void publishNews(String topic, String url, String title, String digest);

    void publishNewsList(List<News> newsList);

}
