package com.kitfrankenstein.newspusher.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kitfrankenstein.newspusher.entity.News;
import com.kitfrankenstein.newspusher.entity.User;
import com.kitfrankenstein.newspusher.service.MqttService;
import com.kitfrankenstein.newspusher.service.NewsService;
import com.kitfrankenstein.newspusher.service.UserService;
import com.kitfrankenstein.newspusher.util.NewsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kit
 * @date: 2019/8/1 18:35
 */
@RestController
@RequestMapping("/News")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private MqttService mqttService;

    @Autowired
    private UserService userService;

    @PostMapping("/getNews")
    public JSONObject getNews(@RequestBody JSONObject json) {
        String source = json.getString("source");
        String lastUrl = json.getString("lastUrl");
        int limit = json.getIntValue("limit");
        List<News> newsList = newsService.getNewsList(source, lastUrl, limit);
        int count = newsList.size();
        JSONObject response = new JSONObject();
        response.put("count", count);
        response.put("result", newsList);
        return response;
    }

    @PostMapping("/getSina")
    public JSONObject getSina(@RequestParam("offset") int offset, @RequestParam("limit") int limit) {
        List<News> newsList = newsService.getSinaNewsList(offset, limit);
        int status = newsList.size();
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("status", status);
        jsonMap.put("result", newsList);
        return jsonMap;
    }

    @PostMapping("/get163")
    public JSONObject get163(@RequestParam("offset") int offset, @RequestParam("limit") int limit) {
        List<News> newsList = newsService.get163NewsList(offset, limit);
        int status = newsList.size();
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("status", status);
        jsonMap.put("result", newsList);
        return jsonMap;
    }

    @PostMapping("/getMChina")
    public JSONObject getMChina(@RequestParam("offset") int offset, @RequestParam("limit") int limit) {
        List<News> newsList = newsService.getMChinaNewsList(offset, limit);
        int status = newsList.size();
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("status", status);
        jsonMap.put("result", newsList);
        return jsonMap;
    }

    @PostMapping("/search")
    public JSONObject searchNews(@RequestBody JSONObject json) {
        String keyword = json.getString("key");
        List<News> newsList = newsService.searchNews(keyword);
        int count = newsList.size();
        JSONObject response = new JSONObject();
        response.put("count", count);
        response.put("result", newsList);
        return response;
    }

    @PostMapping("/getMChinaCount")
    public JSONObject getMChinaCount() {
        int count = newsService.getTableCount(NewsUtil.TABLE_MCHINA);
        JSONObject response = new JSONObject();
        response.put("result", count);
        return response;
    }

    @PostMapping("/getMChinaNews")
    public JSONObject getMChinaNews(@RequestBody JSONObject json) {
        int offset = json.getIntValue("offset");
        int limit = json.getIntValue("limit");
        List<News> newsList = newsService.getMChinaNewsList(offset, limit);
        int count = newsList.size();
        JSONObject response = new JSONObject();
        response.put("count", count);
        response.put("result", newsList);
        return response;
    }

    @PostMapping("/get163Count")
    public JSONObject get163Count() {
        int count = newsService.getTableCount(NewsUtil.TABLE_163);
        JSONObject response = new JSONObject();
        response.put("result", count);
        return response;
    }

    @PostMapping("/get163News")
    public JSONObject get163News(@RequestBody JSONObject json) {
        int offset = json.getIntValue("offset");
        int limit = json.getIntValue("limit");
        List<News> newsList = newsService.get163NewsList(offset, limit);
        int count = newsList.size();
        JSONObject response = new JSONObject();
        response.put("count", count);
        response.put("result", newsList);
        return response;
    }

    @PostMapping("/getSinaCount")
    public JSONObject getSinaCount() {
        int count = newsService.getTableCount(NewsUtil.TABLE_SINA);
        JSONObject response = new JSONObject();
        response.put("result", count);
        return response;
    }

    @PostMapping("/getSinaNews")
    public JSONObject getSinaNews(@RequestBody JSONObject json) {
        int offset = json.getIntValue("offset");
        int limit = json.getIntValue("limit");
        List<News> newsList = newsService.getSinaNewsList(offset, limit);
        int count = newsList.size();
        JSONObject response = new JSONObject();
        response.put("count", count);
        response.put("result", newsList);
        return response;
    }

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public JSONObject push(@RequestBody JSONObject jsonObject) {
        JSONObject response = new JSONObject();
        String phoneNumber = jsonObject.getString("phoneNumber");
        String password = jsonObject.getString("password");
        User user = new User().setPhoneNumber(phoneNumber).setPassword(password);
        if (userService.login(user) != 1) {
            response.put("flag", -1);
            response.put("message", "未登陆");
            return response;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("pushList");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            String topic = json.getString("tag");
            String url = json.getString("url");
            String title = json.getString("title");
            String digest = json.getString("digest");
            mqttService.publishNews(topic, url, title, digest);
        }
        response.put("flag", 1000);
        response.put("message", "推送成功");
        return response;
    }

}
