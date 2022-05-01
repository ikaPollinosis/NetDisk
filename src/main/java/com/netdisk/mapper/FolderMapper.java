package com.netdisk.mapper;

import com.netdisk.entity.File;
import com.netdisk.entity.Folder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @InterfaceName: FolderMapper
 * @Description: 文件夹操作
 * @Date: 2022/4/28 22:09
 */

@Mapper
public interface FolderMapper {
    /**
     * 按文件夹id删除
     * @param folderId
     * @return
     */
    Integer deleteFolderById(Integer folderId);

    /**
     * 按父文件夹id删除
     * @param parentFolderId
     * @return
     */
    Integer deleteFolderByParentFolderId(Integer parentFolderId);

    /**
     * 按文件仓库id删除
     * @param fileStoreId
     * @return
     */
    Integer deleteFolderByFileStoreId(Integer fileStoreId);

    /**
     * 添加文件夹
     * @param folder
     * @return
     */
    Integer addFolder(Folder folder);

    /**
     * 按id获取文件夹
     * @param folderId
     * @return
     */
    Folder getFolderById(Integer folderId);

    /**
     * 获取父文件夹下的文件夹
     * @param parentFolderId
     * @return
     */
    List<Folder> getFolderByParentFolderId(Integer parentFolderId);

    /**
     * 获取文件仓库下的文件夹id
     * @param fileStoreId
     * @return
     */
    List<Folder> getFolderByFileStoreId(Integer fileStoreId);

    /**
     * 统计文件仓库下的文件夹数量
     * @param fileStoreId
     * @return
     */
    Integer getFolderCountByFileStoreId(Integer fileStoreId);

    /**
     * 获取文件仓库根目录下的所有文件夹
     * @param fileStoreId
     * @return
     */
    List<Folder> getRootFoldersByFileStoreId(Integer fileStoreId);

    /**
     * 按id修改文件夹
     * @param folder
     * @return
     */
    Integer updateFolderById(Folder folder);

    /**
     * 按文件夹id获取文件夹下的文件
     * @param fileStoreId
     * @return
     */
    List<File> getFileByFolder(Integer fileStoreId);
}
