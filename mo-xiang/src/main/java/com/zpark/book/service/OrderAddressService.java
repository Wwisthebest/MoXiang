package com.zpark.book.service;

import com.zpark.book.entity.OrderAddress;

public interface OrderAddressService {

    /**
     * 为订单添加地址信息
     * @param orderAddress
     * @return
     */
    boolean addOrderAddress(OrderAddress orderAddress);

    /**
     * 更新订单的地址信息
     * @param orderAddress
     * @return
     */
    boolean updateOrderAddress(OrderAddress orderAddress);

    /**
     * 获取订单的地址信息
     * @param orderId
     * @return
     */
    OrderAddress getOrderAddress(Long orderId);
}
