package com.netdisk.service.impl;

import com.netdisk.mapper.FileMapper;
import com.netdisk.mapper.FileStoreMapper;
import com.netdisk.mapper.FolderMapper;
import com.netdisk.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName: BaseService
 * @Description:
 * @Date: 2022/4/29 15:57
 */
public class BaseService {
    @Autowired
    protected FileMapper fileMapper;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected FolderMapper folderMapper;

    @Autowired
    protected FileStoreMapper fileStoreMapper;

}
