package com.netdisk.mapper;

import com.netdisk.entity.File;
import com.netdisk.entity.FileStoreStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @InterfaceName: FileMapper
 * @Description: 文件操作
 * @Date: 2022/4/28 21:26
 */
@Mapper
public interface FileMapper {
    /**
     * 添加文件
     * @param file
     * @return
     */
    Integer addFile(File file);

    /**
     * 更新文件
     * @param file
     * @return
     */
    Integer updateFileByFileId(File file);

    /**
     * 按文件id删除文件
     * @param fileId
     * @return
     */
    Integer deleteFileByFileId(Integer fileId);

    /**
     * 按父文件夹id删除文件
     * @param parentId
     * @return
     */
    Integer deleteByParentFolderId(Integer parentId);

    /**
     * 按文件id获取文件
     * @param fileId
     * @return
     */
    File getFileByFileId(Integer fileId);

    /**
     * 按文件仓库获取目录下文件
     * @param storeId
     * @return
     */
    List<File> getFilesByFileStoreId(Integer storeId);

    /**
     * 获取父文件夹下的所有文件
     * @param parentId
     * @return
     */
    List<File> getFilesByParentFolderId(Integer parentId);

    /**
     * 按类型获取仓库下的文件
     * @param storeId
     * @param type
     * @return
     */
    List<File> getFilesByType(Integer storeId,Integer type);

    /**
     * 获取仓库的统计信息
     * @param storeId
     * @return
     */
    FileStoreStat getCountStat(Integer storeId);
}
