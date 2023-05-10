package com.tempest.serve.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rimuru Tempest
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo {
    private String username;
    private String password;
}
