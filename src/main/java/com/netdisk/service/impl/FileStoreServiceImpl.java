package com.netdisk.service.impl;

import com.netdisk.entity.FileStore;
import com.netdisk.service.FileStoreService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FileStoreServiceImpl
 * @Description: 文件仓库业务层实现类
 * @Date: 2022/4/29 15:55
 */
@Service
public class FileStoreServiceImpl extends BaseService implements FileStoreService {
    /**
     * 添加文件仓库
     * @param fileStore
     * @return
     */
    @Override
    public Integer addFileStore(FileStore fileStore){
        return fileStoreMapper.addFileStore(fileStore);
    }


    /**
     * 按用户id获取文件仓库
     * @param userId
     * @return
     */
    @Override
    public FileStore getFileStoreByUserId(Integer userId){
        return fileStoreMapper.getFileStoreByUserId(userId);
    }

    /**
     * 按仓库id获取仓库
     * @param storeId
     * @return
     */
    @Override
    public FileStore getFileStoreByStoreId(Integer storeId){
     return fileStoreMapper.getFileStoreByStoreId(storeId);
    }

    /**
     * 增加仓库容量
     * @param storeId
     * @param size
     * @return
     */
    @Override
    public Integer addSize(Integer storeId,Integer size){
       return fileStoreMapper.addSize(storeId,size);
    }

    /**
     * 减少仓库容量
     * @param storeId
     * @param size
     * @return
     */
    @Override
    public Integer subSize(Integer storeId,Integer size){
        return fileStoreMapper.subSize(storeId,size);
    }

    /**
     * 修改仓库权限
     * @param storeId
     * @param permission
     * @param size
     * @return
     */
    @Override
    public Integer updatePermission(Integer storeId,Integer permission,Integer size){
        return fileStoreMapper.updatePermission(storeId,permission,size);
    }

    /**
     * 按仓库id删除仓库
     * @param storeId
     * @return
     */
    @Override
    public Integer deleteByStoreId(Integer storeId){
        return fileStoreMapper.deleteByStoreId(storeId);
    }
}
