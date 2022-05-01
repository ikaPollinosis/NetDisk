package com.netdisk.service.impl;

import com.netdisk.entity.File;
import com.netdisk.entity.FileStoreStat;
import com.netdisk.service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: FileServiceImpl
 * @Description: 文件业务实现类
 * @Date: 2022/4/29 15:56
 */
@Service
public class FileServiceImpl extends BaseService implements FileService {
    /**
     * 按文件id删除文件
     * @param fileId
     * @return
     */
    public Integer deleteFileByFileId(Integer fileId){
        return fileMapper.deleteFileByFileId(fileId);
    }

    /**
     * 按父文件夹id删除文件
     * @param parentId
     * @return
     */
    public Integer deleteByParentFolderId(Integer parentId){
        return fileMapper.deleteFileByFileId(parentId);
    }

    /**
     * 添加文件
     * @param file
     * @return
     */
    public Integer addFile(File file){
        return fileMapper.addFile(file);
    }

    /**
     * 按文件id获取文件
     * @param fileId
     * @return
     */
    public File getFileByFileId(Integer fileId){
        return fileMapper.getFileByFileId(fileId);
    }

    /**
     * 更新文件
     * @param file
     * @return
     */
    public Integer updateFile(File file){
        return fileMapper.updateFileByFileId(file);
    }


    /**
     * 按文件仓库获取目录下文件
     * @param storeId
     * @return
     */
    public List<File> getFilesByFileStoreId(Integer storeId){
        return fileMapper.getFilesByFileStoreId(storeId);
    }

    /**
     * 获取父文件夹下的所有文件
     * @param parentId
     * @return
     */
    public List<File> getFilesByParentFolderId(Integer parentId){
        return fileMapper.getFilesByParentFolderId(parentId);
    }

    /**
     * 按类型获取仓库下的文件
     * @param storeId
     * @param type
     * @return
     */
    public List<File> getFilesByType(Integer storeId,Integer type){
        return fileMapper.getFilesByType(storeId,type);
    }

    /**
     * 获取仓库的统计信息
     * @param storeId
     * @return
     */
    public FileStoreStat getCountStat(Integer storeId){
        FileStoreStat stat=fileMapper.getCountStat(storeId);
        stat.setFolderCount(folderMapper.getFolderCountByFileStoreId(storeId));
        return stat;
    }
}
