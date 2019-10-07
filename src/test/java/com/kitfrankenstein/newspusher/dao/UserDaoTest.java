package com.kitfrankenstein.newspusher.dao;

import com.kitfrankenstein.newspusher.NewsPusherApplicationTests;
import org.junit.Test;

import javax.annotation.Resource;


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