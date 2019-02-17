package com.jpliu.Project.dao;

import com.jpliu.Project.model.Comment;
import com.jpliu.Project.model.Feed;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * dao层 是访问数据库层
 */
@Mapper
public interface FeedDAO {

    String TABLE_NAME = " Feed ";
    String INSERT_FIELDS = " user_id, data, created_date, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME," (", INSERT_FIELDS,
            " ) values (#{userId}, #{data}, #{createdDate}, #{type})"})
    int addFeed(Feed feed);

    //推模式
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Feed getFeedById(int id);

    //拉模式

    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                              @Param("userIds") List<Integer> userIds,
                              @Param("count") int count);
}
