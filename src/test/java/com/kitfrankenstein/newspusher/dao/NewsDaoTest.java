package com.kitfrankenstein.newspusher.dao;

import com.kitfrankenstein.newspusher.NewsPusherApplicationTests;
import com.kitfrankenstein.newspusher.util.MqttPropertiesUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author Kit
 * @date: 2019/8/13 13:33
 */
public class NewsDaoTest extends NewsPusherApplicationTests {

    @Autowired
    private NewsDao newsDao;

    @Test
    public void addNewsList() {
        System.out.println("MqttPropertiesUtil = " + MqttPropertiesUtil.CLEAN_SESSION);
    }
}