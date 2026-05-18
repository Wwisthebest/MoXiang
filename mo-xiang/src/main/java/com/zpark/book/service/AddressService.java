package com.zpark.book.service;

import com.zpark.book.entity.Address;

import java.util.List;

public interface AddressService {
    /**
     * 获取一个用户的所有地址信息
     * @param userId
     * @return
     */
    List<Address> getAddressByUser(Long userId);

    /**
     * 获取单个地址的信息
     * @param id
     * @param userId
     * @return
     */
    Address getAddressById(Long id, Long userId);

    /**
     * 新增地址信息
     * @param address
     * @return
     */
    boolean addAddress(Address address);

    /**
     * 修改地址信息
     * @param address
     * @return
     */
    boolean updateAddress(Address address);

    /**
     * 删除地址信息
     * @param address
     * @return
     */
    boolean deleteAddress(Address address);
}
