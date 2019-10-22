package com.kitfrankenstein.newspusher.component;

import com.kitfrankenstein.newspusher.entity.News;
import com.kitfrankenstein.newspusher.service.MqttService;
import com.kitfrankenstein.newspusher.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kit
 * @date: 2019/8/13 17:46
 */
@Component
public class CrawlTask {

    @Autowired
    private NewsService newsService;

    @Autowired
    private MqttService mqttService;

    @Scheduled(cron = "0 0/20 * * * ?")
    public void crawlMchina() {
        try {
            List<News> newsList = newsService.updateMChina();
            mqttService.publishNewsList(newsList);
        } catch (Exception e) {
            System.out.println("e = " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0/20 * * * ?")
    public void crawl163() {
        try {
            List<News> newsList = newsService.update163();
            mqttService.publishNewsList(newsList);
        } catch (Exception e) {
            System.out.println("e = " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void crawlSina() {
        try {
            List<News> newsList = newsService.updateSina();
            mqttService.publishNewsList(newsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
