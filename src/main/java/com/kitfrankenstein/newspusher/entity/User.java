package com.kitfrankenstein.newspusher.entity;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Kit
 * @date: 2019/8/1 22:25
 */
@Accessors(chain = true)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString
public class User {

    private int id;
    private String phoneNumber;
    private String password;

}
