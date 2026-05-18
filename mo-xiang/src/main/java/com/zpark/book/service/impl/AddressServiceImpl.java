package com.zpark.book.service.impl;

import com.zpark.book.dao.AddressDao;
import com.zpark.book.entity.Address;
import com.zpark.book.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressDao addressDao;

    /**
     * 查询一个用户的所有地址信息
     * 数据库对user_id建索引，加速查询
     * @param userId
     * @return
     */
    public List<Address> getAddressByUser(Long userId){
        return addressDao.getAddressByUser(userId);
    }

    /**
     * 通过地址id查询地址的信息
     * 同时验证用户身份
     * @param id
     * @param userId
     * @return
     */
    public Address getAddressById(Long id, Long userId){
        Address address =addressDao.selectByPrimaryKey(id);
        if(userId.equals(address.getUserId())){
            return address;
        }
        return null;
    }

    /**
     * 新增地址，默认地址的处理逻辑待优化
     * @param address
     * @return
     */
    //取消使用事务，不存在并发修改一个用户的地址信息
    public boolean addAddress(Address address){
        if(address.getDefaultFlag()){
            Address a=new Address();
            a.setDefaultFlag(false);
            a.setUserId(address.getUserId());
            //将一个用户的所有地址改为非默认地址，需要优化，sql增加判断条件default_flag=1，减少更新记录的数目
            addressDao.updateByUserIdSelective(a);
        }else {
            //判断是否有默认地址，若无，则将当前地址设为默认地址
            List<Address> list=addressDao.getDefaultAddress(address.getUserId());
            //可优化，改为count统计，不用返回地址数据，减少io
            if(null==list||0==list.size()){
                address.setDefaultFlag(true);
            }
        }
        return addressDao.insert(address)==1;
    }

    /**
     * 更新地址信息，同时要验证用户身份（未验证）
     *
     * @param address
     * @return
     */
    //取消使用事务，不存在并发修改一个用户的地址信息
    public boolean updateAddress(Address address){
        if(address.getDefaultFlag()){
            //同新增地址时的逻辑
            Address a=new Address();
            a.setDefaultFlag(false);
            a.setUserId(address.getUserId());
            addressDao.updateByUserIdSelective(a);
        }else{
            //若取消默认地址，则将第一个地址设置为默认地址
            List<Address> list=addressDao.getAddressByUser(address.getUserId());
            for(Address a:list){
                if(a.getDefaultFlag()&& a.getId().equals(address.getId())){
                    Address a1=new Address();
                    a1.setId(list.get(0).getId());
                    a1.setDefaultFlag(true);
                    return addressDao.updateByPrimaryKeySelective(address)==1&&
                            addressDao.updateByPrimaryKeySelective(a1)==1;
                }
            }
        }
        return addressDao.updateByPrimaryKeySelective(address)==1;
    }

    /**
     * 删除地址，同时要验证用户身份
     * @param address
     * @return
     */
    public boolean deleteAddress(Address address){
        return addressDao.deleteByPrimaryKeyAndUser(address.getId(), address.getUserId())==1;
    }
}
