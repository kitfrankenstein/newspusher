package com.kitfrankenstein.newspusher.service.impl;

import com.kitfrankenstein.newspusher.NewsPusherApplicationTests;
import com.kitfrankenstein.newspusher.dao.NewsDao;
import com.kitfrankenstein.newspusher.service.NewsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author Kit
 * @date: 2019/8/13 18:02
 */
public class NewsServiceImplTest extends NewsPusherApplicationTests {

    @Autowired
    private NewsService newsService;

    @Test
    public void updateMChina() {
        System.out.println("newsService = " + newsService.updateSina());
    }
}