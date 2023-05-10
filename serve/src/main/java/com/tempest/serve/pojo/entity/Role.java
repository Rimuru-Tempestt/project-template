package com.tempest.serve.pojo.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * (Role)实体类
 *
 * @author RimuruTempest
 * @since 2023/5/09 14:46
 */
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 270133943344937471L;
    /**
     * id
     */
    private Integer id;
    /**
     * 角色名
     */
    private String roleName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}

