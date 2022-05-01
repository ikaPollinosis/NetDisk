package com.netdisk.controller;

import com.netdisk.entity.File;
import com.netdisk.entity.FileStore;
import com.netdisk.entity.Folder;
import com.netdisk.service.FileService;
import com.netdisk.utils.FTPUtil;
import org.apache.commons.net.ftp.FTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: FileStoreController
 * @Description: 文件仓库操作控制器
 * @Date: 2022/4/30 22:42
 */



@Controller
public class FileStoreController extends BaseController{

    private Logger logger= LoggerFactory.getLogger(FileStoreController.class);


    /**
     * 上传文件
     * @param files
     * @param fId
     * @return
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public Map<String,Object>uploadFile(@RequestParam("file")MultipartFile files,@RequestParam Integer fId){
        Map<String ,Object>map=new HashMap<>();
        FileStore store=fileStoreService.getFileStoreByUserId(loginUser.getUserId());
        String name = files.getOriginalFilename().replaceAll(" ","");


        List<File> myFiles = null;
        if (fId == 0){  // 检测是否为根目录
            myFiles = fileService.getFilesByFileStoreId(loginUser.getFileStoreId());
        }else {
            myFiles = fileService.getFilesByParentFolderId(fId);
        }
        for (int i = 0; i < myFiles.size(); i++) {
            if ((myFiles.get(i).getFileName()+myFiles.get(i).getPostfix()).equals(name)){
                logger.error("当前文件重复，上传失败");
                map.put("code", 501);
                return map;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        String path = loginUser.getUserId()+"/"+dateStr +"/"+fId;
        if (!checkTarget(name)){
            logger.error("文件名不合要求，上传失败");
            map.put("code", 502);
            return map;
        }
        Integer sizeInt = Math.toIntExact(files.getSize() / 1024);
        // 检测空间是否足够
        if(store.getCurrentSize()+sizeInt > store.getMaxSize()){
            logger.error("空间已满，上传失败");
            map.put("code", 503);
            return map;
        }
        String size = String.valueOf(files.getSize()/1024.0);
        int indexDot = size.lastIndexOf(".");
        size = size.substring(0,indexDot);
        int index = name.lastIndexOf(".");
        String tempName = name;
        String postfix = "";
        int type = 4;
        if (index!=-1){
            tempName = name.substring(index);
            name = name.substring(0,index);
            type = getType(tempName.toLowerCase());
            postfix = tempName.toLowerCase();
        }
        try {
            // 上传FTP服务器
            boolean b = FTPUtil.uploadFile("/"+path, name + postfix, files.getInputStream());
            if (b){
                logger.info("文件上传成功"+files.getOriginalFilename());
                fileService.addFile(
                        File.builder()
                                .fileName(name).fileStoreId(loginUser.getFileStoreId()).filePath(path)
                                .downloadCount(0).uploadTime(new Date()).parentFolderId(fId).
                                size(Integer.valueOf(size)).type(type).postfix(postfix).build());
                //  增加已经使用的空间
                fileStoreService.addSize(store.getFileStoreId(),Integer.valueOf(size));
                try {
                    Thread.sleep(5000);
                    map.put("code", 200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                logger.error("文件上传失败"+files.getOriginalFilename());
                map.put("code", 504);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;

    }

    /**
     * 删除文件
     * @param fId
     * @param folder
     * @return
     */
    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam Integer fId,Integer folder){
        // 获取文件
        File file=fileService.getFileByFileId(fId);
        String remote=file.getFilePath();
        String fileName=file.getFileName()+file.getPostfix();
        boolean flag=FTPUtil.deleteFile("/"+remote,fileName);   // 在FTP服务器中删除
        if(flag){
            fileStoreService.subSize(file.getFileStoreId(),Integer.valueOf(file.getSize()));
            fileService.deleteFileByFileId(fId);    // 在数据库中删除
        }
        else {logger.info("删除文件"+file+"失败");}
        logger.info("删除文件"+file+"成功");
        return "redirect:/files?fId="+folder;
    }


    /**
     * 正则检查文件名
     * @param target
     * @return
     */
    public boolean checkTarget(String target) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_.]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(target);
        return !matcher.find();
    }

    @GetMapping("/downloadFile")
    public String downloadFile(@RequestParam Integer fId){
        // 权限检查
        if(fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission()>=2){
            logger.error("权限不足，下载失败");
            return "redirect:/error401Page";
        }
        File file=fileService.getFileByFileId(fId);
        String remote =file.getFilePath();
        String fileName=file.getFileName()+file.getPostfix();
        try{
            OutputStream outputStream=new BufferedOutputStream(response.getOutputStream());
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition","attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8"));
            boolean flag= FTPUtil.downloadFile("/"+remote,fileName,outputStream);
            if(flag) {
                // 下载次数+1
                fileService.updateFile(File.builder().fileId(file.getFileId()).downloadCount(file.getDownloadCount() + 1).build());
                outputStream.flush();
                outputStream.close();
                logger.info("文件" + file + "下载成功");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "success";
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
     * 重命名文件夹
     * @param folder
     * @param map
     * @return
     */
    @PostMapping("/updateFolder")
    public String updateFolder(Folder folder,Map<String, Object> map) {
        Folder fileFolder = folderService.getFolderById(folder.getFolderId());
        fileFolder.setFolderName(folder.getFolderName());
        List<Folder> folders = folderService.getFolderByParentFolderId(fileFolder.getParentFolderId());
        for (int i = 0; i < folders.size(); i++) {
            Folder folder1 = folders.get(i);
            if (folder1.getFolderName().equals(folder.getFolderName())&&folder1.getFolderId()!=folder.getFolderId()){
                logger.info("该文件夹已存在");
                return "redirect:/files?error=1&fId="+fileFolder.getParentFolderId();
            }
        }
        // 向数据库写入数据
        Integer integer = folderService.updateFolderById(folder);
        logger.info("重命名文件夹"+folder+"成功");
        return "redirect:/files?fId="+folder.getParentFolderId();
    }

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
        folderService.deleteFolderById(folder.getFolderId());
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
