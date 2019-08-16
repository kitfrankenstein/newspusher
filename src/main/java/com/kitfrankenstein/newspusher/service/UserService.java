package com.kitfrankenstein.newspusher.service;

import com.kitfrankenstein.newspusher.entity.User;

/**
 * @author Kit
 * @date: 2019/8/12 15:06
 */
public interface UserService {
    int login(User user);
    boolean register(User user);
}
