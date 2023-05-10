package com.tempest.serve.pojo.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author RimuruTempest
 * @since 2023/5/09 14:43
 */
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -11034988266473250L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 编码后的密码
     */
    private String password;
    /**
     * 账号是否过期
     */
    private Boolean isExpired;
    /**
     * 账号是否被锁定
     */
    private Boolean isLocked;
    /**
     * 是否被删除
     */
    private Boolean isDelete;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

}

