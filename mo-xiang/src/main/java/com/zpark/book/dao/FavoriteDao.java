package com.zpark.book.dao;

import com.zpark.book.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FavoriteDao {
    int deleteByPrimaryKey(Long id);

    int insert(Favorite record);

    int insertSelective(Favorite record);

    Favorite selectByPrimaryKey(Long id);

    List<Favorite> getMyFavorite(Long userId);

    Integer checkFavorite(Long userId,Long idleId);

    int updateByPrimaryKeySelective(Favorite record);

    int updateByPrimaryKey(Favorite record);
}