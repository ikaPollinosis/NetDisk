package com.netdisk.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: File
 * @Description: 文件实体类
 * @Date: 2022/4/28 20:02
 */
@AllArgsConstructor
@Data
@Builder
public class File implements Serializable {
    private Integer fileId;

    private String fileName;

    private Integer fileStoreId;

    private String filePath;

    private Integer downloadCount;

    private Date uploadTime;

    private Integer parentFolderId;

    private Integer size;

    private Integer type;

    private Integer postfix;
}
