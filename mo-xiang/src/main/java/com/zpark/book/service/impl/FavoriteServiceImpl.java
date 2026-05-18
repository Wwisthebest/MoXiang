package com.zpark.book.service.impl;

import com.zpark.book.dao.FavoriteDao;
import com.zpark.book.dao.IdleItemDao;
import com.zpark.book.entity.Favorite;
import com.zpark.book.entity.IdleItem;
import com.zpark.book.service.FavoriteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Resource
    private FavoriteDao favoriteDao;

    @Resource
    private IdleItemDao idleItemDao;

    /**
     * 新增收藏
     * @param favorite
     * @return
     */
    public boolean addFavorite(Favorite favorite){
        return favoriteDao.insert(favorite)==1;
    }

    /**
     * 删除收藏
     * @param id
     * @return
     */
    public boolean deleteFavorite(Long id){
        return favoriteDao.deleteByPrimaryKey(id)==1;
    }

    /**
     * 判断用户是否收藏某个闲置
     * user_id建索引
     * @param userId
     * @param idleId
     * @return
     */
    public Integer isFavorite(Long userId,Long idleId){
        return favoriteDao.checkFavorite(userId,idleId);
    }

    /**
     * 查询一个用户的所有收藏
     * 关联查询，没有用join，通过where in查询关联的闲置信息
     * @param userId
     * @return
     */
    public List<Favorite> getAllFavorite(Long userId){
        List<Favorite> list=favoriteDao.getMyFavorite(userId);
        if(list.size()>0){
            List<Long> idleIdList=new ArrayList<>();
            for(Favorite i:list){
                idleIdList.add(i.getIdleId());
            }
            List<IdleItem> idleItemList =idleItemDao.findIdleByList(idleIdList);
            Map<Long, IdleItem> map=new HashMap<>();
            for(IdleItem idle: idleItemList){
                map.put(idle.getId(),idle);
            }
            for(Favorite i:list){
                i.setIdleItem(map.get(i.getIdleId()));
            }
        }
        return list;
    }
}