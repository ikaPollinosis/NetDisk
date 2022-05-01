package com.netdisk.controller;

import com.netdisk.entity.File;
import com.netdisk.entity.FileStore;
import com.netdisk.entity.Folder;
import com.netdisk.service.FileService;
import com.netdisk.utils.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: FileStoreController
 * @Description: 文件仓库操作控制器
 * @Date: 2022/4/30 22:42
 */



@Controller
public class FileStoreController extends BaseController{

    private Logger logger= LoggerFactory.getLogger(FileStoreController.class);

    /**
     * 删除文件夹
     * @param fId
     * @return
     */
    @GetMapping("/deleteFolder")
    public String deleteFolder(@RequestParam Integer fId){
        Folder folder = folderService.getFolderById(fId);
        deleteFolderForce(folder);   // 进行递归删除
        return folder.getParentFolderId() == 0?"redirect:/files":"redirect:/files?fId="+folder.getParentFolderId(); // 重定向至父文件夹
    }


    /**
     * 新建文件夹
     * @param folder
     * @param map
     * @return
     */
    @PostMapping("/addFolder")
    public String addFolder(Folder folder,Map<String, Object> map) {
        // 设置文件夹
        folder.setFileStoreId(loginUser.getFileStoreId());
        folder.setTime(new Date());
        // 检查当前文件夹是否已经存在
        List<Folder> folders = null;
        if (folder.getParentFolderId() == 0){
            // 向用户根目录添加文件夹
            folders = folderService.getRootFoldersByFileStoreId(loginUser.getFileStoreId());
        }else{
            // 向用户的其他目录添加文件夹
            folders = folderService.getFolderByParentFolderId(folder.getParentFolderId());
        }
        for (int i = 0; i < folders.size(); i++) {
            Folder fileFolder = folders.get(i);
            if (fileFolder.getFolderName().equals(folder.getFolderName())){
                logger.info("该文件夹已存在");
                return "redirect:/files?error=1&fId="+folder.getParentFolderId();
            }
        }
        // 向数据库写入数据
        Integer integer = folderService.addFolder(folder);
        logger.info("添加文件夹"+folder+"成功");
        return "redirect:/files?fId="+folder.getParentFolderId();
    }

    /**
     * 对文件夹进行递归删除
     * @param folder
     */
    public void deleteFolderForce(Folder folder){
        // 获取文件夹下的所有子文件夹
        List<Folder> folders=folderService.getFolderByParentFolderId(folder.getFolderId());
        // 获取文件夹下的所有文件
        List<File> files= fileService.getFilesByParentFolderId(folder.getFolderId());
        // 文件夹下的文件不为空
        if(!files.isEmpty()){
            for(int i=0;i<files.size();i++){
                Integer id=files.get(i).getFileId();
                // 请求ftp服务器删除文件
                boolean flag= FTPUtil.deleteFile("/"+files.get(i).getFilePath(), files.get(i).getFileName() + files.get(i).getPostfix());
                if(flag){
                    fileService.deleteFileByFileId(files.get(i).getFileId());   // 数据库中删除
                    fileStoreService.subSize(folder.getFileStoreId(),Integer.valueOf(files.get(i).getSize()));   // 恢复文件仓库容量
                }
            }
        }
        if(folders.size()!=0) {
            for (int i = 0; i <= folders.size(); i++) {
                // 递归删除子文件夹
                deleteFolderForce(folders.get(i));
            }
        }
    }


    /**
     * 根据扩展名返回文件类型
     * @param type
     * @return
     */
    public int getType(String type){
        if (".chm".equals(type)||".txt".equals(type)||".xmind".equals(type)||".xlsx".equals(type)||".md".equals(type)
                ||".doc".equals(type)||".docx".equals(type)||".pptx".equals(type)
                ||".wps".equals(type)||".word".equals(type)||".html".equals(type)||".pdf".equals(type)){
            return  1;
        }else if (".bmp".equals(type)||".gif".equals(type)||".jpg".equals(type)||".ico".equals(type)||".vsd".equals(type)
                ||".pic".equals(type)||".png".equals(type)||".jepg".equals(type)||".jpeg".equals(type)||".webp".equals(type)
                ||".svg".equals(type)){
            return 2;
        } else if (".avi".equals(type)||".mov".equals(type)||".qt".equals(type)
                ||".asf".equals(type)||".rm".equals(type)||".navi".equals(type)||".wav".equals(type)
                ||".mp4".equals(type)||".mkv".equals(type)||".webm".equals(type)){
            return 3;
        } else if (".mp3".equals(type)||".wma".equals(type)){
            return 4;
        } else {
            return 5;
        }
    }

}
