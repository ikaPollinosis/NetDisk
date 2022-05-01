package com.netdisk.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: UserDisplay
 * @Description: 用户数据展示类
 * @Date: 2022/4/28 20:00
 */
@AllArgsConstructor
@Data
@Builder
public class UserDisplay implements Serializable {
    private Integer userId;

    private String userName;

    private String email;

    private Date registerTime;

    private String imagePath;

    private Integer current;

    private Integer maxSize;

    private Integer permission;
}
