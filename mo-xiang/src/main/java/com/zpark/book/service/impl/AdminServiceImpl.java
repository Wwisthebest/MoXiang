package com.zpark.book.service.impl;

import com.zpark.book.dao.AdminDao;
import com.zpark.book.entity.Admin;
import com.zpark.book.service.AdminService;
import com.zpark.book.vo.PageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminDao adminDao;

    @Override
    public Admin login(String accountNumber, String adminPassword){
        return adminDao.login(accountNumber,adminPassword);
    }

    @Override
    public PageVo<Admin> getAdminList(int page, int nums){
        List<Admin> list=adminDao.getList((page-1)*nums,nums);
        int count=adminDao.getCount();
        return new PageVo<>(list,count);
    }

    @Override
    public boolean addAdmin(Admin admin){
        return adminDao.insert(admin)==1;
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        return adminDao.updateByPrimaryKey(admin)==1;
    }

}
