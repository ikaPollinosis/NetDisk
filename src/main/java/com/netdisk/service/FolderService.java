package com.netdisk.service;

import com.netdisk.entity.File;
import com.netdisk.entity.Folder;

import java.util.List;

/**
 * @InterfaceName: FolderService
 * @Description: 文件夹业务接口
 * @Date: 2022/4/29 15:51
 */
public interface FolderService {
    /**
     * 添加文件夹
     * @param folder
     * @return
     */
    Integer addFolder(Folder folder);

    /**
     * 按文件夹id删除
     * @param folderId
     * @return
     */
    Integer deleteFolderById(Integer folderId);

    /**
     * 按文件夹id获取文件夹下的文件
     * @param fileStoreId
     * @return
     */
    List<File> getFileByFolder(Integer fileStoreId);

    /**
     * 获取父文件夹下的文件夹
     * @param parentFolderId
     * @return
     */
    List<Folder> getFolderByParentFolderId(Integer parentFolderId);

    /**
     * 按id获取文件夹
     * @param folderId
     * @return
     */
    Folder getFolderById(Integer folderId);

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
}
