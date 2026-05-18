package com.zpark.book.dao;

import com.zpark.book.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User userLogin(@Param("accountNumber") String accountNumber, @Param("userPassword") String userPassword);

    User selectByPrimaryKey(Long id);

    Long selectByUserName(String nickname);

    List<User> getUserList();

    List<User> findUserByList(List<Long> idList);

    List<User> getNormalUser(int begin, int nums);

    List<User> getBanUser(int begin, int nums);

    // 通过账户查找用户
    List<User> getUserByNumber(String searchValue, int mode);

    int countNormalUser();

    int countBanUser();

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int updatePassword(@Param("newPassword") String newPassword,
                       @Param("oldPassword") String oldPassword,@Param("id") Long id);

    int updateUserInfo(User updateUser);

    /**
     * 根据邮箱查询用户（返回单个用户，适用于唯一约束场景）
     * @param email 邮箱地址
     * @return 用户对象（存在则不为 null）
     */
    User selectByEmail(@Param("email") String email);

    User selectByPhone(@Param("phone") String phone);
    /**
     * 根据手机号查询用户数量
     * @param phone 手机号
     * @return 用户数量
     */
    int countByPhone(@Param("phone") String phone);

    /**
     * 根据手机号和密码查询用户
     * @param phone 手机号
     * @param password 密码
     * @return 用户对象
     */
    User selectByPhoneAndPassword(
            @Param("phone") String phone,
            @Param("password") String password
    );

    /**
     * 根据邮箱和密码查询用户
     * @param email 邮箱地址
     * @param password 密码
     * @return 用户对象
     */
    User selectByEmailAndPassword(
            @Param("email") String email,
            @Param("password") String password
    );
}