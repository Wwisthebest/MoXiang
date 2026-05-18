package com.zpark.book.dao;

import com.zpark.book.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageDao {
    int deleteByPrimaryKey(Long id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Long id);

    List<Message> getMyMessage(Long userId);

    List<Message> getIdleMessage(Long idleId);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);
}