package com.netdisk.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FileStore
 * @Description: 文件存储实体类
 * @Date: 2022/4/28 19:58
 */
@AllArgsConstructor
@Data
@Builder
public class FileStore implements Serializable {
    private Integer fileStoreId;

    private Integer userId;

    private Integer currentSize;

    private Integer maxSize;

    private Integer permission;
}
