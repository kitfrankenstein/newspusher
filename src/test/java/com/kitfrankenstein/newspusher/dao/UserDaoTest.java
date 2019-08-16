package com.kitfrankenstein.newspusher.dao;

import com.kitfrankenstein.newspusher.NewsPusherApplicationTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author Kit
 * @date: 2019/8/1 22:34
 */
public class UserDaoTest extends NewsPusherApplicationTests {

    @Resource
    private UserDao userDao;

    @Test
    public void getUserByPhone() {
        System.out.println("userDao = " + userDao.getUserByPhone("15521041271"));
    }

    @Test
    public void saveUser() {
    }
}