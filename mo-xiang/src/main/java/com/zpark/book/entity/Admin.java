package com.zpark.book.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * sh_admin
 * @author 
 */
public class Admin implements Serializable {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 管理员账号
     */
    private String accountNumber;

    /**
     * 密码
     */
    private String adminPassword;

    /**
     * 管理员名字
     */
    private String adminName;

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
     * 性别
     */
    private String sex;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birth;

    // 当你一个类实现了Serializable接口，就会有显式地定义serialVersionUID
    // 序列化时为了保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性。
    private static final long serialVersionUID = 1L;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Admin other = (Admin) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))

                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getAccountNumber() == null ? other.getAccountNumber() == null : this.getAccountNumber().equals(other.getAccountNumber()))
            && (this.getAdminPassword() == null ? other.getAdminPassword() == null : this.getAdminPassword().equals(other.getAdminPassword()))
            && (this.getAdminName() == null ? other.getAdminName() == null : this.getAdminName().equals(other.getAdminName()))
                && (this.getBirth() == null ? other.getBirth() == null : this.getBirth().equals(other.getBirth()))
                && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAccountNumber() == null) ? 0 : getAccountNumber().hashCode());
        result = prime * result + ((getAdminPassword() == null) ? 0 : getAdminPassword().hashCode());
        result = prime * result + ((getAdminName() == null) ? 0 : getAdminName().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getBirth() == null) ? 0 : getBirth().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
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

        sb.append(", accountNumber=").append(accountNumber);
        sb.append(", adminPassword=").append(adminPassword);
        sb.append(", adminName=").append(adminName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append(", birth=").append(birth);
        sb.append(", sex=").append(sex);
        sb.append("]");
        return sb.toString();
    }
}