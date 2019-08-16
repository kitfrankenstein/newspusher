package com.kitfrankenstein.newspusher.dao;

import com.kitfrankenstein.newspusher.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Kit
 * @date: 2019/8/12 16:19
 */
@Mapper
public interface NewsDao {

    int addNews(@Param("news") News news, @Param("table") String table);

    int addNewsList(@Param("newsList") List<News> newsList, @Param("table") String table);

    int getCount(@Param("table") String table);

    int getNewsRow(@Param("newsUrl") String newsUrl, @Param("table") String table);

    List<News> getNewsListByOffset(@Param("table") String table,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    List<News> getNewsListByTime(@Param("table") String table, @Param("time") Timestamp timestamp);

    List<News> searchNews(@Param("keyword") String keyword);

}
