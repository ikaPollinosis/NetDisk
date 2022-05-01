package com.netdisk.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: User
 * @Description: 用户实体类
 * @Date: 2022/4/28 20:00
 */
@AllArgsConstructor
@Data
@Builder
public class User implements Serializable {
    private Integer userId;

    private String openId;

    private Integer fileStoreId;

    private String userName;

    private String email;

    private String password;

    private Date registerTime;

    private String imagePath;

    private Integer role;
}
