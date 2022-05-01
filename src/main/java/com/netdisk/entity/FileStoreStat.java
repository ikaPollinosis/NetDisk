package com.netdisk.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @ClassName: FileStoreStat
 * @Description: 存储情况统计类
 * @Date: 2022/4/28 20:00
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FileStoreStat implements Serializable {
    private FileStore fileStore;

    private int doc;

    private int video;

    private int image;

    private int other;

    private int fileCount;

    private int folderCount;
}
