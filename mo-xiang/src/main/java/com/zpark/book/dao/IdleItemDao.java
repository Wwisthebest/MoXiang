package com.zpark.book.dao;

import com.zpark.book.entity.Admin;
import com.zpark.book.entity.IdleItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IdleItemDao {
    int deleteByPrimaryKey(Long id);

    int insert(IdleItem record);

    int insertSelective(IdleItem record);

    IdleItem selectByPrimaryKey(Long id);

    List<IdleItem> getAllIdleItem(Long userId);

    int countIdleItem(String findValue);

    int countIdleItemByLable(int idleLabel);

    int countIdleItemByStatus(int status);

    List<IdleItem> findIdleItem(String findValue, int begin, int nums);

    List<IdleItem> findIdleItem1(String findValue, int status, int begin, int nums);

    List<IdleItem> findIdleItemByLable(int idleLabel, int begin, int nums);

    List<IdleItem> getIdleItemByStatus(int status, int begin, int nums);

    int updateByPrimaryKeySelective(IdleItem record);

    int updateByPrimaryKey(IdleItem record);

    List<IdleItem> findIdleByList(List<Long> idList);

    Admin getDetail(Long id);
}
