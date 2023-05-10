package com.tempest.serve.pojo.dto;

import com.tempest.serve.pojo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rimuru Tempest
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
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
    /**
     * 角色列表
     */
    private List<Role> roles;
}
