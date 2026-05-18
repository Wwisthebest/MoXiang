package com.zpark.book.service;

import com.zpark.book.entity.User;
import com.zpark.book.vo.PageVo;

public interface UserService {

    // 保持原有方法定义
    User getUser(Long id);
    User userLogin(String accountNumber, String userPassword);
    boolean userSignIn(User user);
    boolean updateUserInfo(User user);
    boolean updatePassword(String newPassword, String oldPassword, Long id);
    PageVo<User> getUserByStatus(int status, int page, int nums);
    Long getUserId(String nickname);
    PageVo<User> getUserByNumber(String searchValue, int mode);
    boolean isEmailExists(String email);
    boolean isPhoneExists(String phone);

    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param status 状态值（0=正常，1=封禁）
     * @return 更新成功返回true
     */
    boolean updateUserStatus(Long userId, Byte status);

    // 可以保留这两个方法，也可以删除（取决于是否需要直接调用）
    User loginByPhone(String phone, String password);
    User loginByEmail(String email, String password);

    // 新增方法：根据手机号查询用户（不验证密码）
    User getUserByPhone(String phone);

    // 新增方法：根据邮箱查询用户（不验证密码）
    User getUserByEmail(String email);

    // 新增方法：验证密码
    boolean verifyPassword(User user, String inputPassword);

    // 新增方法：更新登录时间
    void updateLoginTime(Long userId);
}