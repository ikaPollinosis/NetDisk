package com.netdisk.controller;

import com.netdisk.entity.File;
import com.netdisk.entity.FileStoreStat;
import com.netdisk.entity.Folder;
import com.netdisk.service.FileService;
import com.netdisk.service.impl.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

/**
 * @ClassName: PageController
 * @Description: 页面跳转控制器
 * @Date: 2022/4/30 16:36
 */
@Controller
public class PageController extends BaseController {
    Logger logger = LoggerFactory.getLogger(PageController.class);

    /**
     * 用户主页
     * @param map
     * @return
     */
    @GetMapping("/index")
    public String index(Map<String, Object> map) {
        //获得统计信息
        FileStoreStat stats = fileService.getCountStat(loginUser.getFileStoreId());
        stats.setFileStore(fileStoreService.getFileStoreByStoreId(loginUser.getFileStoreId()));
        map.put("statistics", stats);
        return "userpages/index";
    }


    /**
     * 前往网盘页面
     * @param fId
     * @param fName
     * @param error
     * @param map
     * @return
     */
    @GetMapping("/files")
    public String toFileStorePage(Integer fId, String fName, Integer error, Map<String, Object> map) {
        // 判断是否包含错误信息
        if (error != null) {
            if (error == 1) {
                map.put("error", "添加失败！当前已存在同名文件夹");
            }
            if (error == 2) {
                map.put("error", "重命名失败！文件夹已存在");
            }
        }
        // 包含的子文件夹
        List<Folder> folders = null;
        // 包含的文件
        List<File> files = null;
        // 当前文件夹信息
        Folder nowFolder = null;
        // 当前文件夹的相对路径
        List<Folder> location = new ArrayList<>();
        if (fId == null || fId <= 0) {
            // 代表当前为根目录
            fId = 0;
            folders = folderService.getRootFoldersByFileStoreId(loginUser.getFileStoreId());
            files = fileService.getFilesByFileStoreId(loginUser.getFileStoreId());
            nowFolder = Folder.builder().folderId(fId).build();
            location.add(nowFolder);
        } else {
            // 当前为具体目录,访问的文件夹不是当前登录用户所创建的文件夹
            Folder folder = folderService.getFolderById(fId);
            if (folder.getFileStoreId() - loginUser.getFileStoreId() != 0){
                return "redirect:/error401Page";
            }
            // 当前为具体目录，访问的文件夹是当前登录用户所创建的文件夹
            folders = folderService.getFolderByParentFolderId(fId);
            files = fileService.getFilesByParentFolderId(fId);
            nowFolder = folderService.getFolderById(fId);
            // 遍历查询当前目录
            Folder temp = nowFolder;
            while (temp.getParentFolderId() != 0) {
                temp = folderService.getFolderById(temp.getParentFolderId());
                location.add(temp);
            }
        }
        Collections.reverse(location);
        // 获取统计信息
        FileStoreStat stats = fileService.getCountStat(loginUser.getFileStoreId());
        map.put("statistics", stats);
        map.put("permission", fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission());
        map.put("folders", folders);
        map.put("files", files);
        map.put("nowFolder", nowFolder);
        map.put("location", location);
        logger.info("网盘页面域中的数据:" + map);
        return "userpages/files";
    }

    @GetMapping("/user")
    public String toUserPage(Map<String, Object> map) {
        //获得统计信息
        FileStoreStat statistics = fileService.getCountStat(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        return "userpages/user";
    }

}
