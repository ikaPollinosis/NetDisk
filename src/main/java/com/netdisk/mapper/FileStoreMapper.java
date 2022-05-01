package com.netdisk.mapper;

import com.netdisk.entity.File;
import com.netdisk.entity.FileStore;
import com.netdisk.entity.FileStoreStat;
import org.apache.ibatis.annotations.Mapper;

/**
 * @InterfaceName: FileStoreMapper
 * @Description: 文件仓库操作
 * @Date: 2022/4/28 21:42
 */
@Mapper
public interface FileStoreMapper {
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
