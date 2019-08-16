package com.kitfrankenstein.newspusher.service.impl;

import com.kitfrankenstein.newspusher.dao.UserDao;
import com.kitfrankenstein.newspusher.entity.User;
import com.kitfrankenstein.newspusher.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kit
 * @date: 2019/8/12 15:12
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public int login(User user) {
        User existUser = userDao.getUserByPhone(user.getPhoneNumber());
        if (existUser == null) {
            return 0;//user not found
        }
        if (existUser.getPassword().equals(user.getPassword())) {
            return 1;//user found
        }
        return -1;//wrong password
    }

    @Override
    public boolean register(User user) {
        return userDao.saveUser(user) == 1;
    }
}
