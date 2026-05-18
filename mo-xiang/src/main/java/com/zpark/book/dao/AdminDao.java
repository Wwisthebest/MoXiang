package com.zpark.book.dao;

import com.zpark.book.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminDao {
    int deleteByPrimaryKey(Long id);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    Admin login(@Param("accountNumber") String accountNumber, @Param("adminPassword") String adminPassword);

    List<Admin> getList(int begin, int nums);

    int getCount();

    int updateAdmin(Admin admin);
}
