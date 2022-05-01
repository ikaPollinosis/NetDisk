package com.netdisk.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: Folder
 * @Description: 文件夹实体类
 * @Date: 2022/4/28 19:52
 */

@AllArgsConstructor
@Data
@Builder
public class Folder implements Serializable {
    private Integer folderId;

    private String folderName;

    private Integer parentFolderId;

    private Integer fileStoreId;

    private Date time;
}
