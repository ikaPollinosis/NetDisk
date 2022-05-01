package com.netdisk.service.impl;

import com.netdisk.entity.File;
import com.netdisk.entity.Folder;
import com.netdisk.service.FolderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: FolderServiceImpl
 * @Description: 文件夹业务实现类
 * @Date: 2022/4/29 15:56
 */
@Service
public class FolderServiceImpl extends BaseService implements FolderService {
    /**
     * 添加文件夹
     * @param folder
     * @return
     */
    @Override
    public Integer addFolder(Folder folder){
        return folderMapper.addFolder(folder);
    }

    /**
     * 按文件夹id删除
     * @param folderId
     * @return
     */
    @Override
    public Integer deleteFolderById(Integer folderId){
        return folderMapper.deleteFolderById(folderId);
    }

    /**
     * 按文件夹id获取文件夹下的文件
     * @param fileStoreId
     * @return
     */
    @Override
    public List<File> getFileByFolder(Integer fileStoreId){
        return folderMapper.getFileByFolder(fileStoreId);
    }

    /**
     * 获取父文件夹下的文件夹
     * @param parentFolderId
     * @return
     */
    @Override
    public List<Folder> getFolderByParentFolderId(Integer parentFolderId){
        return folderMapper.getFolderByParentFolderId(parentFolderId);
    }

    /**
     * 按id获取文件夹
     * @param folderId
     * @return
     */
    @Override
    public Folder getFolderById(Integer folderId){
        return folderMapper.getFolderById(folderId);
    }

    /**
     * 获取文件仓库根目录下的所有文件夹
     * @param fileStoreId
     * @return
     */
    @Override
    public List<Folder> getRootFoldersByFileStoreId(Integer fileStoreId){
        return folderMapper.getRootFoldersByFileStoreId(fileStoreId);
    }

    /**
     * 按id修改文件夹
     * @param folder
     * @return
     */
    @Override
    public Integer updateFolderById(Folder folder){
        return folderMapper.updateFolderById(folder);
    }
}
