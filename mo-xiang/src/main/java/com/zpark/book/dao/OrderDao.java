package com.zpark.book.dao;

import com.zpark.book.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    List<Order> getMyOrder(Long userId);

    List<Order> getAllOrder(int begin, int nums);

    List<Order> getOrderByNumber(String searchValue, int begin, int nums);

    int countAllOrder();


    List<Order> findOrderByIdleIdList(List<Long> idleIdList);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}