package com.zpark.book.service.impl;

import com.zpark.book.dao.OrderAddressDao;
import com.zpark.book.entity.OrderAddress;
import com.zpark.book.service.OrderAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderAddressServiceImpl implements OrderAddressService {

    @Resource
    private OrderAddressDao orderAddressDao;

    /**
     * 为订单新增地址信息
     * @param orderAddress
     * @return
     */
    public boolean addOrderAddress(OrderAddress orderAddress){
        return orderAddressDao.insert(orderAddress)==1;
    }

    /**
     * 更新订单的地址信息，未验证用户身份
     * @param orderAddress
     * @return
     */
    public boolean updateOrderAddress(OrderAddress orderAddress){
        orderAddress.setOrderId(null);
        return orderAddressDao.updateByPrimaryKeySelective(orderAddress)==1;
    }

    /**
     * 获取订单的地址信息
     * orderId建索引
     * @param orderId
     * @return
     */
    public OrderAddress getOrderAddress(Long orderId){
        return orderAddressDao.selectByOrderId(orderId);
    }
}
