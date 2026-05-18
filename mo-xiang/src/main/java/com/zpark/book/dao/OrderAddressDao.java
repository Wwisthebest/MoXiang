package com.zpark.book.dao;

import com.zpark.book.entity.OrderAddress;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderAddressDao {
    int deleteByPrimaryKey(Long id);

    int insert(OrderAddress record);

    int insertSelective(OrderAddress record);

    OrderAddress selectByPrimaryKey(Long id);

    OrderAddress selectByOrderId(Long orderId);

    int updateByPrimaryKeySelective(OrderAddress record);

    int updateByPrimaryKey(OrderAddress record);
}