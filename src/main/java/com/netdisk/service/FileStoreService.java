package com.netdisk.service;

import com.netdisk.entity.FileStore;

/**
 * @InterfaceName: FileStoreService
 * @Description: 文件仓库业务接口
 * @Date: 2022/4/29 15:51
 */
public interface FileStoreService {
    /**
     * 添加文件仓库
     * @param fileStore
     * @return
     */
    public Integer addFileStore(FileStore fileStore);

    /**
     * 按用户id获取文件仓库
     * @param userId
     * @return
     */
    public FileStore getFileStoreByUserId(Integer userId);

    /**
     * 按仓库id获取仓库
     * @param storeId
     * @return
     */
    public FileStore getFileStoreByStoreId(Integer storeId);

    /**
     * 增加仓库容量
     * @param storeId
     * @param size
     * @return
     */
    public Integer addSize(Integer storeId,Integer size);

    /**
     * 减少仓库容量
     * @param storeId
     * @param size
     * @return
     */
    public Integer subSize(Integer storeId,Integer size);

    /**
     * 修改仓库权限
     * @param storeId
     * @param permission
     * @param size
     * @return
     */
    public Integer updatePermission(Integer storeId,Integer permission,Integer size);

    /**
     * 按仓库id删除仓库
     * @param storeId
     * @return
     */
    public Integer deleteByStoreId(Integer storeId);


}
