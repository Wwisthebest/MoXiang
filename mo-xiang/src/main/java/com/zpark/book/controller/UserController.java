package com.zpark.book.controller;

import com.zpark.book.enums.ErrorMsg;
import com.zpark.book.entity.User;
import com.zpark.book.service.UserService;
import com.zpark.book.utils.MailUtil;
import com.zpark.book.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MailUtil mailUtil;

    // 存储邮箱验证码（实际应用中建议使用 Redis）
    private static final Map<String, String> emailVerificationCodeMap = new ConcurrentHashMap<>();
    private static final long EMAIL_CODE_EXPIRATION = 5 * 60 * 1000; // 5分钟有效期
    private static final Map<String, Long> emailCodeCreationTime = new ConcurrentHashMap<>();

    // ========================
    // ========= 注册 ==========
    // ========================

    /**
     * 手机注册（无需验证码）
     */
    @PostMapping("phone-register")
    public ResultVo phoneRegister(@RequestBody @Valid User user) {
        String phone = user.getPhone();

        // 验证手机号格式（已处理 phone 为 null 的情况）
        if (!isValidPhone(phone)) {
            return ResultVo.fail(ErrorMsg.ACCOUNT_LEN);
        }

        // 检查手机号是否已注册
        if (userService.isPhoneExists(phone)) {
            return ResultVo.fail(ErrorMsg.ACCOUNT_EXIT);
        }

        // 密码加密
        String password = user.getUserPassword();
        if (password != null && !password.isEmpty()) {
            user.setUserPassword(encryptPassword(password));
        }

        // 设置用户信息
        user.setSignInTime(new Timestamp(System.currentTimeMillis()));
        user.setAvatar(user.getAvatar() != null ? user.getAvatar() : "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        //user.setRegisterType("phone"); // 记录注册类型

        // 执行注册
        if (userService.userSignIn(user)) {
            return ResultVo.success(user);
        }

        return ResultVo.fail(ErrorMsg.REGISTER_ERROR);
    }

    @PostMapping("email-register")
    public ResultVo emailRegister(@RequestBody @Valid User user) {
        String email = user.getEmail();
        String phone = user.getPhone();
        String verificationCode = user.getVerificationCode();

        // 验证邮箱格式
        if (!isValidEmail(email)) {
            return ResultVo.fail(ErrorMsg.INVALID_EMAIL_FORMAT);
        }

        // 处理手机号：如果为空字符串，设置为null（避免唯一约束冲突）
        if (phone != null && phone.trim().isEmpty()) {
            user.setPhone(null);
        } else if (phone != null) {
            // 检查手机号是否已注册（仅当手机号非空时）
            if (userService.isPhoneExists(phone)) {
                return ResultVo.fail(ErrorMsg.ACCOUNT_EXIT);
            }
        }

        // 检查邮箱是否已注册
        if (userService.isEmailExists(email)) {
            return ResultVo.fail(ErrorMsg.EMAIL_ALREADY_EXISTS);
        }

        // 验证邮箱验证码
        if (!verifyEmailCode(email, verificationCode)) {
            return ResultVo.fail(ErrorMsg.INVALID_VERIFICATION_CODE);
        }

        // 密码加密
        String password = user.getUserPassword();
        if (password != null && !password.isEmpty()) {
            user.setUserPassword(encryptPassword(password));
        }

        // 设置用户信息
        user.setSignInTime(new Timestamp(System.currentTimeMillis()));
        user.setAvatar(user.getAvatar() != null ? user.getAvatar() : "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");

        // 执行注册
        try {
            if (userService.userSignIn(user)) {
                // 注册成功后移除验证码
                emailVerificationCodeMap.remove(email);
                emailCodeCreationTime.remove(email);
                return ResultVo.success(user);
            }
        } catch (DuplicateKeyException e) {
            // 处理可能的数据库唯一约束冲突（防御性措施）
            log.error("注册失败：数据库唯一约束冲突", e);
            return ResultVo.fail(ErrorMsg.REGISTER_ERROR);
        }

        return ResultVo.fail(ErrorMsg.REGISTER_ERROR);
    }

    //邮件发送
    @PostMapping("email-code")
    public ResultVo sendEmailVerificationCode(@RequestBody @Valid User.EmailRequest request) {
        String email = request.getEmail();
        // 检查邮箱是否已注册
        if (userService.isEmailExists(email)) {
            return ResultVo.fail(ErrorMsg.EMAIL_ALREADY_EXISTS);
        }

        // 生成验证码
        String code = generateVerificationCode();
        emailVerificationCodeMap.put(email, code);
        emailCodeCreationTime.put(email, System.currentTimeMillis());

        try {
            boolean sent = mailUtil.sendMail(
                    email,
                    "【图书管理系统】注册验证码",
                    "您的注册验证码是：" + code + "\n有效期5分钟，请尽快完成注册。"
            );

            if (sent) {
                Map<String, String> result = new HashMap<>();
                result.put("email", email);
                result.put("message", "验证码已发送，有效期5分钟");
                return ResultVo.success(result);
            } else {
                // 发送失败，移除缓存的验证码
                emailVerificationCodeMap.remove(email);
                emailCodeCreationTime.remove(email);
                return ResultVo.fail(ErrorMsg.EMAIL_SEND_ERROR);
            }
        } catch (Exception e) {
            // 记录异常堆栈
            log.error("发送邮箱验证码时发生未知异常: {}", e.getMessage(), e);

            // 移除缓存的验证码
            emailVerificationCodeMap.remove(email);
            emailCodeCreationTime.remove(email);

            return ResultVo.fail(ErrorMsg.EMAIL_SEND_ERROR);
        }
    }

    // ========================
    // ========= 登录 ==========
    // ========================

    /**
     * 手机号登录接口
     */
    @PostMapping("phone-login")
    public ResultVo phoneLogin(@RequestBody Map<String, String> loginData, HttpServletResponse response) {
        log.info("手机号登录请求: {}", loginData);

        // 从请求中提取参数
        String phone = loginData.get("account");
        String password = loginData.get("password");

        // 参数校验
        if (phone == null || phone.isEmpty() || password == null || password.isEmpty()) {
            log.warn("登录参数缺失");
            return ResultVo.fail(ErrorMsg.MISSING_PARAMETER);
        }

        // 验证手机号格式
        if (!isValidPhone(phone)) {
            log.warn("无效的手机号格式: {}", phone);
            return ResultVo.fail(ErrorMsg.INVALID_ACCOUNT_TYPE);
        }

        try {
            // 查询用户是否存在（不验证密码）
            User user = userService.getUserByPhone(phone);

            if (user == null) {
                log.info("用户不存在: {}", phone);
                return ResultVo.fail(ErrorMsg.USER_NOT_EXIST);
            }

            // 检查用户状态
            if (user.getUserStatus() != null && user.getUserStatus() == 1) {
                log.info("用户已被封禁: {}", phone);
                return ResultVo.fail(ErrorMsg.ACCOUNT_Ban);
            }

            // 验证密码
            boolean passwordMatch = verifyPassword(password, user.getUserPassword());
            if (!passwordMatch) {
                log.info("密码错误: {}，错误的密码:{}", phone, password);
                return ResultVo.fail(ErrorMsg.PASSWORD_ERROR);
            }

            // 更新登录时间
            userService.updateLoginTime(user.getId());

            // 处理登录结果并设置Cookie
            return processLogin(user, password, response, ErrorMsg.USER_NOT_EXIST);

        } catch (DataAccessException e) {
            // 数据库访问异常
            log.error("数据库操作异常: {}", phone, e);
            return ResultVo.fail(ErrorMsg.DATABASE_ERROR);
        } catch (Exception e) {
            // 其他未知异常
            log.error("登录过程中发生未知异常: {}", phone, e);
            return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
        }
    }

    /**
     * 邮箱登录接口
     */
    @PostMapping("email-login")
    public ResultVo emailLogin(@RequestBody Map<String, String> loginData, HttpServletResponse response) {
        log.info("邮箱登录请求: {}", loginData);

        // 从请求中提取参数（注意：使用 email 字段而不是 account）
        String email = loginData.get("email");  // 修改为 email
        String password = loginData.get("password");

        // 参数校验
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            log.warn("登录参数缺失: email={}, password={}", email, password);
            return ResultVo.fail(ErrorMsg.MISSING_PARAMETER);
        }

        // 验证邮箱格式
        if (!isValidEmail(email)) {
            log.warn("无效的邮箱格式: {}", email);
            return ResultVo.fail(ErrorMsg.INVALID_EMAIL_FORMAT);
        }

        try {
            // 查询用户是否存在（不验证密码）
            User user = userService.getUserByEmail(email);

            if (user == null) {
                log.info("用户不存在: {}", email);
                return ResultVo.fail(ErrorMsg.USER_NOT_EXIST);
            }

            // 检查用户状态
            if (user.getUserStatus() != null && user.getUserStatus() == 1) {
                log.info("用户已被封禁: {}", email);
                return ResultVo.fail(ErrorMsg.ACCOUNT_Ban);
            }

            // 验证密码
            boolean passwordMatch = verifyPassword(password, user.getUserPassword());
            if (!passwordMatch) {
                log.info("密码错误: {}，错误的密码:{}", email, password);
                return ResultVo.fail(ErrorMsg.PASSWORD_ERROR);
            }

            // 更新登录时间
            userService.updateLoginTime(user.getId());

            // 处理登录结果并设置Cookie
            return processLogin(user, password, response, ErrorMsg.USER_NOT_EXIST);

        } catch (DataAccessException e) {
            // 数据库访问异常
            log.error("数据库操作异常: {}", email, e);
            return ResultVo.fail(ErrorMsg.DATABASE_ERROR);
        } catch (Exception e) {
            // 其他未知异常
            log.error("登录过程中发生未知异常: {}", email, e);
            return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
        }
    }

    /**
     * 统一处理登录结果
     */
    private ResultVo processLogin(User user, String password, HttpServletResponse response, ErrorMsg errorMsg) {
        if (user == null) {
            return ResultVo.fail(errorMsg);
        }

        // 密码错误
        if (!verifyPassword(password, user.getUserPassword())) {
            return ResultVo.fail(ErrorMsg.PASSWORD_ERROR);
        }

        // 账号封禁
        if (user.getUserStatus() != null && user.getUserStatus().equals((byte) 1)) {
            return ResultVo.fail(ErrorMsg.ACCOUNT_Ban);
        }

        // 设置Cookie
        Cookie cookie = new Cookie("shUserId", String.valueOf(user.getId()));
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(3600 * 24); // 24小时有效期
        response.addCookie(cookie);

        return ResultVo.success(user);
    }

    // ========================
    // ======== 工具方法 ========
    // ========================

    /**
     * 生成6位验证码
     */
    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    /**
     * 验证手机号格式
     */
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 验证邮箱格式
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * 验证邮箱验证码（修复NPE问题）
     */
    private boolean verifyEmailCode(String email, String code) {
        // 检查验证码长度
        if (code == null || code.length() != 6 || !code.matches("\\d{6}")) {
            return false;
        }

        // 检查邮箱是否存在验证码
        if (!emailVerificationCodeMap.containsKey(email)) {
            return false;
        }

        // 获取并验证验证码（先验证，再移除）
        String storedCode = emailVerificationCodeMap.get(email);
        long creationTime = emailCodeCreationTime.get(email);

        // 检查有效期
        if (System.currentTimeMillis() - creationTime > EMAIL_CODE_EXPIRATION) {
            emailVerificationCodeMap.remove(email);
            emailCodeCreationTime.remove(email);
            return false;
        }

        // 验证验证码是否匹配
        boolean isValid = storedCode.equals(code);

        // 无论是否匹配，验证后都移除验证码（防止重放攻击）
        if (isValid) {
            emailVerificationCodeMap.remove(email);
            emailCodeCreationTime.remove(email);
        }

        return isValid;
    }

    /**
     * MD5密码加密
     */
    private String encryptPassword(String password) {
        try {
            // 创建MD5加密对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将密码转换为字节数组并进行加密
            byte[] messageDigest = md.digest(password.getBytes());

            // 创建十六进制字符串
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);

            // 如果加密结果不足32位，前面补0
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5加密失败: {}", e.getMessage(), e);
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * 验证密码
     */
    private boolean verifyPassword(String inputPassword, String storedPassword) {
        // 对输入密码进行加密后再比较
        String encryptedInput = encryptPassword(inputPassword);
        return encryptedInput.equals(storedPassword);
    }

    // ========================
    // ======== 原有接口 ========
    // ========================

    /**
     * 退出登录
     */
    @RequestMapping("logout")
    public ResultVo logout(HttpServletResponse response) {
        // 清除shUserId Cookie，设置值为空，立即过期
        Cookie cookie = new Cookie("shUserId", null);
        cookie.setMaxAge(0); // 立即过期
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        // 可以同时清除其他相关Cookie（如果有）
        Cookie tokenCookie = new Cookie("token", null);
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath("/");
        tokenCookie.setHttpOnly(false);
        response.addCookie(tokenCookie);

        return ResultVo.success();
    }

    /**
     * 获取用户信息
     */
    @GetMapping("info")
    public ResultVo getOneUser(@CookieValue(value = "shUserId", required = false) String id) {
        if (id == null) {
            return ResultVo.fail(ErrorMsg.USER_NOT_LOGGED_IN);
        }

        try {
            User user = userService.getUser(Long.valueOf(id));
            if (user == null) {
                return ResultVo.fail(ErrorMsg.USER_NOT_EXIST);
            }
            return ResultVo.success(user);
        } catch (NumberFormatException e) {
            return ResultVo.fail(ErrorMsg.INVALID_USER_ID);
        }
    }

    /**
     * 修改用户公开信息
     * @param id
     * @param user
     * @return
     */
    @PostMapping("/info")
    public ResultVo updateUserPublicInfo(
            @CookieValue(value = "shUserId", required = false) String id,
            @RequestBody User user
    ) {
        if (id == null) {
            return ResultVo.fail(ErrorMsg.USER_NOT_LOGGED_IN);
        }

        try {
            user.setId(Long.valueOf(id));

            // 打印接收的参数（调试用）
            System.out.println("接收用户数据：" + user);

            if (userService.updateUserInfo(user)) {
                return ResultVo.success();
            }
            return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
        } catch (NumberFormatException e) {
            return ResultVo.fail(ErrorMsg.INVALID_USER_ID);
        }
    }


    /**
     * 修改密码
     */
    @GetMapping("/password")
    public ResultVo updateUserPassword(
            @CookieValue(value = "shUserId", required = false) String id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        if (id == null) {
            return ResultVo.fail(ErrorMsg.USER_NOT_LOGGED_IN);
        }

        try {
            // 对新密码进行加密
            String encryptedNewPassword = encryptPassword(newPassword);

            if (userService.updatePassword(encryptedNewPassword, oldPassword, Long.valueOf(id))) {
                return ResultVo.success();
            }
            return ResultVo.fail(ErrorMsg.PASSWORD_RESET_ERROR);
        } catch (NumberFormatException e) {
            return ResultVo.fail(ErrorMsg.INVALID_USER_ID);
        }
    }
}
