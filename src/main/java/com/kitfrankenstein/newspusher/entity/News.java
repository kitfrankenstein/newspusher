package com.kitfrankenstein.newspusher.entity;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Kit
 * @date: 2019/8/12 16:13
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString
public class News {

    private String url;
    private String title;
    private String digest;
    private String time;
    private String imgUrl;
    private String tag;

}
