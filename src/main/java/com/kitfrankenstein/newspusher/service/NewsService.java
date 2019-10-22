package com.kitfrankenstein.newspusher.service;

import com.kitfrankenstein.newspusher.entity.News;

import java.util.List;

/**
 * @author Kit
 * @date: 2019/8/12 15:06
 */
public interface NewsService {

    List<News> updateSina();

    List<News> update163();

    List<News> updateMChina();

    int getTableCount(String table);

    List<News> getSinaNewsList(int offset, int limit);

    List<News> get163NewsList(int offset, int limit);

    List<News> getMChinaNewsList(int offset, int limit);

    List<News> getNewsList(String table, String lastUrl, int limit);

    List<News> searchNews(String keyword);

}
