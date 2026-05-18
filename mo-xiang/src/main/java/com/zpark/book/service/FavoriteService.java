package com.zpark.book.service;

import com.zpark.book.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    /**
     * 添加收藏
     * @param favorite
     * @return
     */
    boolean addFavorite(Favorite favorite);

    /**
     * 取消收藏
     * @param id
     * @return
     */
    boolean deleteFavorite(Long id);

    /**
     * 判断是否收藏
     * @param userId
     * @param idleId
     * @return
     */
    Integer isFavorite(Long userId,Long idleId);

    /**
     * 获取收藏列表
     * @param userId
     * @return
     */
    List<Favorite> getAllFavorite(Long userId);
}
