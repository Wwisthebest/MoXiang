package com.zpark.book.service;

import com.zpark.book.entity.Admin;
import com.zpark.book.vo.PageVo;

public interface AdminService {

    Admin login(String accountNumber, String adminPassword);

    PageVo<Admin> getAdminList(int page, int nums);

    boolean addAdmin(Admin admin);

    boolean updateAdmin(Admin admin);
}
