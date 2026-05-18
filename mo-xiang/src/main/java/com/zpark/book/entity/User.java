package com.zpark.book.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * sh_user
 * @author chao
 */
public class User implements Serializable {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 手机号（手机注册专用，唯一标识）
     */
    @NotNull(message = "手机号不能为空")
    private String phone;

    /**
     * 邮箱（邮箱注册专用，唯一标识）
     */
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 登录密码
     */
    private String userPassword;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 注册时间
     */
    private Date signInTime;

    /**
     * 用户状态（如：正常/封禁）
     */
    private Byte userStatus;


    /**
     * 性别
     */
    private String sex;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birth;

    private String verificationCode; // 新增字段：验证码
    private static final long serialVersionUID = 1L;

    // ---------------------
    // 内部类：处理邮箱请求参数（如发送验证码）
    // ---------------------
    public static class EmailRequest implements Serializable {
        private static final long serialVersionUID = 1L;

        @NotEmpty(message = "邮箱地址不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        // Getter 和 Setter
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    // ---------------------
    // Getter 和 Setter 方法
    // ---------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 手机号 getter/setter
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 邮箱 getter/setter
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(Date signInTime) {
        this.signInTime = signInTime;
    }

    public Byte getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Byte userStatus) {
        this.userStatus = userStatus;
    }
    // 新增getter/setter
    public String getVerificationCode() {
        return verificationCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }


    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
    // ---------------------
    // equals、hashCode、toString 方法（包含 phone 和 email）
    // ---------------------

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        User other = (User) that;
        return
                (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId())) &&
                        (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone())) &&
                        (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail())) &&
                        (this.getUserPassword() == null ? other.getUserPassword() == null : this.getUserPassword().equals(other.getUserPassword())) &&
                        (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname())) &&
                        (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar())) &&
                        (this.getSignInTime() == null ? other.getSignInTime() == null : this.getSignInTime().equals(other.getSignInTime())) &&
                        (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex())) &&
                        (this.getBirth() == null ? other.getBirth() == null : this.getBirth().equals(other.getBirth()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getUserPassword() == null) ? 0 : getUserPassword().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getSignInTime() == null) ? 0 : getSignInTime().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getBirth() == null) ? 0 : getBirth().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", userPassword=").append(userPassword);
        sb.append(", nickname=").append(nickname);
        sb.append(", avatar=").append(avatar);
        sb.append(", signInTime=").append(signInTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append(", sex=").append(sex);
        sb.append(", birth=").append(birth);
        sb.append("]");
        return sb.toString();
    }
}