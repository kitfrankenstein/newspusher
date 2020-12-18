package com.kitfrankenstein.newspusher.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kitfrankenstein.newspusher.dao.NewsDao;
import com.kitfrankenstein.newspusher.entity.News;
import com.kitfrankenstein.newspusher.service.NewsService;
import com.kitfrankenstein.newspusher.util.NewsUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kit
 * @date: 2019/8/13 13:29
 */
@CacheConfig(cacheNames = "news", keyGenerator = "keyGenerator")
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;

    @CacheEvict(allEntries = true)
    @Override
    public List<News> updateSina() {
        List<News> newsList = new ArrayList<>();
        String sinaAPI = "http://cre.dp.sina.cn/api/v3/get?cre=tianyi&mod={0}&merge=3&statics=1&length=10";
        String[] mods = {"wnews", "wmil", "wtech", "wfin", "wedu",
                "wspt", "went", "wgame", "whealth"};
        String[] tags = {"recommend", "military", "technology", "finance", "education", "sport",
                "entertainment", "game", "health"};
        //新闻军事科技财经教育体育娱乐游戏健康
        try {
            for (int i = 0; i < mods.length; i++) {
                String jsonString = Jsoup.connect(MessageFormat.format(sinaAPI, mods[i]))
                        .userAgent(NewsUtil.USER_AGENT)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute()
                        .body()
                        .trim();
                JSONObject jsonObject = JSON.parseObject(jsonString);
                //取jsonArray
                JSONArray jA = jsonObject.getJSONArray("data");
                for (int j = 0; j < jA.size(); j++) {
                    JSONObject jO = jA.getJSONObject(j);
                    //手机新浪网json格式
                    //标题（title或mtitle）、url（url）、内容提要（intro或无）
                    //来源（media或无、时间（ctime或mtime）、题图（thumb或mthumbs或thumbs)
                    String url = jO.getString("url");
                    if (url.startsWith("http")) {
                        String title = jO.containsKey("title")
                                ? jO.getString("title")
                                : jO.getString("mtitle");
                        String desc = jO.containsKey("intro")
                                ? NewsUtil.subLength(jO.getString("intro"), 40)
                                : "";
                        String media = jO.containsKey("media")
                                ? jO.getString("media")
                                : "新浪网";
                        String imageUrl =
                                jO.containsKey("thumb") ? jO.getString("thumb")
                                    : (jO.containsKey("mthumbs") ? jO.getJSONArray("mthumbs").getString(0)
                                        : (jO.containsKey("thumbs") ? jO.getJSONArray("thumbs").getString(0)
                                            : null));
                        String time = media + " " +
                                (jO.containsKey("ctime")
                                        ? NewsUtil.stampToDateString(jO.getString("ctime"))
                                        : NewsUtil.stampToDateString(jO.getString("mtime")));
                        News news = new News(url, title, desc, time, imageUrl, tags[i]);
                        newsList.add(news);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("e = " + e.getMessage());
        }
        newsDao.addNewsList(newsList, NewsUtil.TABLE_SINA);
        return newsList;
    }

    @CacheEvict(allEntries = true)
    @Override
    public List<News> update163() {
        List<News> newsList = new ArrayList<>();
        String[] categories = {"BBM54PGAwangning", "BAI67OGGwangning", "BA8EE5GMwangning",
                "BA8D4A3Rwangning", "BA8E6OEOwangning"};
        //, "BA10TA81wangning", "BDC4QSV3wangning"};
        //新闻军事财经科技体育//娱乐健康
        String[] tags = {"recommend", "military", "finance", "technology", "sport"};
        String src = "http://3g.163.com/touch/reconstruct/article/list/";
        //手机网易网json数据源
        try {
            //每个分类爬二十条
            for (int i = 0; i < categories.length; i++) {
                String temp = Jsoup.connect(src + categories[i] + "/0-20.html")
                        .userAgent(NewsUtil.USER_AGENT)
                        .get()
                        .text()
                        .trim();
                String jsonString = temp.substring(temp.indexOf('(') + 1, temp.lastIndexOf(')'));
                JSONObject jsonObject = JSON.parseObject(jsonString);
                JSONArray jA = jsonObject.getJSONArray(categories[i]);
                for (int j = 0; j < jA.size(); j++) {
                    JSONObject jO = jA.getJSONObject(j);
                    //手机网易json格式->内容（标签）：标题（title）、url（url）、内容提要（digest）、
                    //时间和来源（ptime和source）、题图（imgsrc）
                    String url = jO.getString("url");
                    if (url.startsWith("http")) {
                        String title = jO.getString("title");
                        if (jO.containsKey("photosetID")) {
                            url = jO.getString("skipURL");
                        }
                        String digest = jO.getString("digest").replace('\n', ' ');
                        String time = jO.getString("source") + " " + jO.getString("ptime");
                        String imageUrl = jO.getString("imgsrc");
                        News news = new News(url, title, digest, time, imageUrl, tags[i]);
                        newsList.add(news);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("e = " + e.getMessage());
        }
        newsDao.addNewsList(newsList, NewsUtil.TABLE_163);
        return newsList;
    }

    @CacheEvict(allEntries = true)
    @Override
    public List<News> updateMChina() {
        List<News> newsList = new ArrayList<>();
        String[] cols = {"3", "39", "18", "11", "8", "14", "12"};
        //推荐要闻国际财经军事科技教育
        String[] tags = {"recommend", "focus", "international", "finance", "military",
                "technology", "education"};
        String src = "https://k1.m.china.com.cn/scene/query/list?columnId={0}&page=0&size=30&appId=chinaApp";
        try {
            //每个分类爬二十条
            for (int i = 0; i < cols.length; i++) {
                String jsonString = Jsoup.connect(MessageFormat.format(src, cols[i]))
                        .userAgent(NewsUtil.USER_AGENT)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute()
                        .body()
                        .trim();
                JSONObject jsonObject = JSON.parseObject(jsonString);
                JSONArray jA = jsonObject.getJSONArray("list");
                for (int j = 0; j < jA.size(); j++) {
                    JSONObject jO = jA.getJSONObject(j);
                    if (jO.containsKey("pubTime")) {
                        String title = jO.getString("title");
                        String url = jO.getString("artUrl");
                        String digest = NewsUtil.subLength(jO.getString("describe"), 40);
                        String time = jO.getString("sourceName") + " " + NewsUtil.stampToDateString(jO.getString("pubTime"));
                        String imageUrl = null;
                        if (jO.containsKey("images")) {
                            imageUrl = jO.getJSONArray("images").getString(0);
                        }
                        News news = new News(url, title, digest, time, imageUrl, tags[i]);
                        newsList.add(news);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("e = " + e.getMessage());
        }
        newsDao.addNewsList(newsList, NewsUtil.TABLE_MCHINA);
        return newsList;
    }

    @Cacheable
    @Override
    public int getTableCount(String table) {
        return newsDao.getCount(table);
    }

    @Cacheable
    @Override
    public List<News> getSinaNewsList(int offset, int limit) {
        return newsDao.getNewsListByOffset(NewsUtil.TABLE_SINA, offset, limit);
    }

    @Cacheable
    @Override
    public List<News> get163NewsList(int offset, int limit) {
        return newsDao.getNewsListByOffset(NewsUtil.TABLE_163, offset, limit);
    }

    @Cacheable
    @Override
    public List<News> getMChinaNewsList(int offset, int limit) {
        return newsDao.getNewsListByOffset(NewsUtil.TABLE_MCHINA, offset, limit);
    }

    @Cacheable
    @Override
    public List<News> getNewsList(String table, String lastUrl, int limit) {
        if ("163".equals(table)) {
            table = NewsUtil.TABLE_163;
        }
        return newsDao.getNewsListByOffset(table, newsDao.getNewsRow(lastUrl, table), limit);
    }

    @Cacheable
    @Override
    public List<News> searchNews(String keyword) {
        return newsDao.searchNews(keyword);
    }
}
