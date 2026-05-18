package com.zpark.book.service.impl;

import com.zpark.book.dao.UserDao;
import com.zpark.book.entity.User;
import com.zpark.book.enums.ErrorMsg;
import com.zpark.book.service.UserService;
import com.zpark.book.vo.PageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;


    @Override
    public boolean updateUserStatus(Long userId, Byte status) {
        log.info("接收到封号请求：userId={}, status={}", userId, status);

        if (userId == null || userId <= 0) {
            log.warn("无效的用户ID：{}", userId);
            return false;
        }
        try {
            // 创建用户对象
            User user = new User();
            user.setId(userId);
            user.setUserStatus(status);

            // 执行更新
            int rows = userDao.updateUserInfo(user);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新用户状态失败", e);
        }
    }

    /**
     * 查询一个用户的公开信息
     */
    @Override
    public User getUser(Long id) {
        log.info("查询用户ID: {}", id);
        User user = userDao.selectByPrimaryKey(id);
        if (user == null) {
            log.warn("用户不存在，ID: {}", id);
        }
        return user;
    }

    /**
     * 统一登录接口
     */
    @Override
    public User userLogin(String accountNumber, String userPassword) {
        log.info("统一登录请求: {}", accountNumber);

        // 参数校验
        if (accountNumber == null || userPassword == null) {
            log.warn("登录参数为空，account: {}, password: {}",
                    accountNumber, (userPassword == null ? null : "******"));
            return null;
        }

        // 判断是手机号还是邮箱
        if (accountNumber.matches("^1[3-9]\\d{9}$")) {
            return loginByPhone(accountNumber, userPassword);
        } else if (accountNumber.contains("@")) {
            return loginByEmail(accountNumber, userPassword);
        }

        log.warn("无效的账号格式: {}", accountNumber);
        return null; // 无效的账号格式
    }

    /**
     * 手机号登录
     */
    @Override
    public User loginByPhone(String phone, String password) {
        log.info("手机号登录请求: {}", phone);

        // 参数校验
        if (phone == null || password == null) {
            log.warn("手机号或密码为空，phone: {}, password: {}",
                    phone, (password == null ? null : "******"));
            return null;
        }

        try {
            // 查询用户
            User user = userDao.selectByPhone(phone);

            if (user == null) {
                log.info("手机号不存在: {}", phone);
                return null;
            }

            // 检查用户状态（假设0为正常，1为封禁）
            if (user.getUserStatus() != null && user.getUserStatus() == 1) {
                log.info("用户已被封禁: {}", phone);
                return null;
            }

            // 明文密码校验
            if (password.equals(user.getUserPassword())) {
                log.info("手机号登录成功: {}", phone);
                return user;
            } else {
                log.info("密码错误: {}", phone);
                return null;
            }
        } catch (Exception e) {
            log.error("手机号登录异常: {}", phone, e);
            return null;
        }
    }

    /**
     * 邮箱登录
     */
    @Override
    public User loginByEmail(String email, String password) {
        log.info("邮箱登录请求: {}", email);

        // 参数校验
        if (email == null || password == null) {
            log.warn("邮箱或密码为空，email: {}, password: {}",
                    email, (password == null ? null : "******"));
            return null;
        }

        try {
            // 查询用户
            User user = userDao.selectByEmail(email);

            if (user == null) {
                log.info("邮箱不存在: {}", email);
                return null;
            }

            // 检查用户状态
            if (user.getUserStatus() != null && user.getUserStatus() == 1) {
                log.info("用户已被封禁: {}", email);
                return null;
            }

            // 明文密码校验
            if (password.equals(user.getUserPassword())) {
                log.info("邮箱登录成功: {}", email);
                return user;
            } else {
                log.info("密码错误: {}", email);
                return null;
            }
        } catch (Exception e) {
            log.error("邮箱登录异常: {}", email, e);
            return null;
        }
    }

    /**
     * 注册
     */
    @Override
    public boolean userSignIn(User user) {
        log.info("用户注册请求: {}", user.getPhone() != null ? user.getPhone() : user.getEmail());

        try {
            boolean result = userDao.insert(user) == 1;
            log.info("用户注册结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("用户注册异常: {}", user, e);
            return false;
        }
    }

    /**
     *修改用户公开信息，未验证用户身份
     * @param user
     * @return
     */
    public boolean updateUserInfo(User user){
//        return userDao.updateByPrimaryKeySelective(user)==1;
        // 只允许更新公开信息字段，避免密码等敏感信息被更新
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setNickname(user.getNickname());
        updateUser.setSex(user.getSex());
        updateUser.setBirth(user.getBirth());
        updateUser.setEmail(user.getEmail());
        updateUser.setAvatar(user.getAvatar());
        updateUser.setPhone(user.getPhone());


        return userDao.updateUserInfo(updateUser) == 1;
    }

    /**
     * 修改密码
     */
    @Override
    public boolean updatePassword(String newPassword, String oldPassword, Long id) {
        log.info("修改用户密码: {}", id);

        try {
            // 明文密码修改
            return userDao.updatePassword(newPassword, oldPassword, id) == 1;
        } catch (Exception e) {
            log.error("修改密码异常: {}", id, e);
            return false;
        }
    }

    /**
     * 分页获取用户列表
     */
    @Override
    public PageVo<User> getUserByStatus(int status, int page, int nums) {
        log.info("分页获取用户列表，状态: {}, 页码: {}, 每页数量: {}", status, page, nums);

        try {
            List<User> list;
            int count = 0;
            if (status == 0) {
                count = userDao.countNormalUser();
                list = userDao.getNormalUser((page - 1) * nums, nums);
            } else {
                count = userDao.countBanUser();
                list = userDao.getBanUser((page - 1) * nums, nums);
            }
            return new PageVo<>(list, count);
        } catch (Exception e) {
            log.error("分页获取用户列表异常，状态: {}", status, e);
            return new PageVo<>();
        }
    }

    /**
     * 通过用户的姓名查询用户的id
     */
    @Override
    public Long getUserId(String nickname) {
        log.info("通过昵称查询用户ID: {}", nickname);
        try {
            return userDao.selectByUserName(nickname);
        } catch (Exception e) {
            log.error("通过昵称查询用户ID异常: {}", nickname, e);
            return null;
        }
    }

    /**
     * 通过手机号搜索用户
     */
    @Override
    public PageVo<User> getUserByNumber(String searchValue, int mode) {
        log.info("通过手机号搜索用户: {}, 模式: {}", searchValue, mode);
        try {
            List<User> list = userDao.getUserByNumber(searchValue, mode - 1);
            return new PageVo<>(list, list.size()); // 返回实际数量
        } catch (Exception e) {
            log.error("通过手机号搜索用户异常: {}", searchValue, e);
            return new PageVo<>();
        }
    }

    /**
     * 检查邮箱是否已注册
     */
    @Override
    public boolean isEmailExists(String email) {
        try {
            User user = userDao.selectByEmail(email);
            return user != null;
        } catch (Exception e) {
            log.error("检查邮箱是否存在异常: {}", email, e);
            return false;
        }
    }

    /**
     * 检查手机号是否已注册
     */
    @Override
    public boolean isPhoneExists(String phone) {
        try {
            return userDao.countByPhone(phone) > 0;
        } catch (Exception e) {
            log.error("检查手机号是否存在异常: {}", phone, e);
            return false;
        }
    }

    @Override
    public User getUserByPhone(String phone) {
        return userDao.selectByPhone(phone);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.selectByEmail(email);
    }

//    @Override
//    public boolean verifyPassword(User user, String inputPassword) {
//        // 明文密码比对
//        return inputPassword.equals(user.getUserPassword());
//    }
@Override
public boolean verifyPassword(User user, String inputPassword) {
    String dbPassword = user.getUserPassword();

    // 添加详细日志
    log.info("输入密码: [{}], 长度: {}", inputPassword, inputPassword.length());

    // 检查数据库密码是否为空
    if (dbPassword == null) {
        log.warn("数据库密码为null");
        return false;
    }

    log.info("数据库密码: [{}], 长度: {}", dbPassword, dbPassword.length());

    // 打印字符编码信息
    log.info("输入密码字节: {}", Arrays.toString(inputPassword.getBytes()));
    log.info("数据库密码字节: {}", Arrays.toString(dbPassword.getBytes()));

    // 打印十六进制表示
    log.info("输入密码十六进制: {}", bytesToHex(inputPassword.getBytes()));
    log.info("数据库密码十六进制: {}", bytesToHex(dbPassword.getBytes()));

    // 执行比较并记录结果
    boolean isMatch = inputPassword.equals(dbPassword);
    log.info("密码比对结果: {}", isMatch);

    return isMatch;
}

    @Override
    public void updateLoginTime(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setSignInTime(new Timestamp(System.currentTimeMillis()));
        userDao.updateByPrimaryKeySelective(user);
    }
    private String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}