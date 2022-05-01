package com.netdisk.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * @ClassName: FTPUtil
 * @Description: FTP类
 * @Date: 2022/4/27 20:43
 */
public class FTPUtil {
    /**
     * 主机名
     */
    private static String HOST = "localhost";
    /**
     * FTP端口
     */
    private static int PORT = 21;
    /**
     * 账号和登录密码
     */
    private static String USERNAME = "anonymous";  // 匿名登录
    private static String PASSWORD = "287720499@qq.com";  // 邮箱作为匿名登录密码
    /**
     * FTP主目录
     */
    private static String BASEDIR = "";
    /**
     * FTP客户端
     */
    private static FTPClient ftp;

    public static boolean initFTPClient() {
        /**
         * @Description: FTP服务初始化
         * @Param: []
         * @return: boolean
         * @Date: 2022/4/27
         */
        ftp = new FTPClient();
        int reply;  // 服务器返回代码
        try {
            ftp.connect(HOST, PORT);    // 连接FTP服务器
            ftp.login(USERNAME, PASSWORD);  // 登入
            ftp.setBufferSize(10240);   // 缓冲区大小为10KB
            ftp.setDataTimeout(100000); // 数据超时为10s
            ftp.setConnectTimeout(200000);  //连接超时20s
            ftp.setFileStructure(FTP.BINARY_FILE_TYPE); //二进制传输
            reply = ftp.getReplyCode();
            if (!FTPReply.isProtectedReplyCode(reply)) {  // FTP服务器连接失败
                ftp.disconnect();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean uploadFile(String filepath, String filename, InputStream instream) {
    /**
    * @Description:  上传文件
    * @Param: [filepath, filename, instream]
    * @return: boolean
    * @Date: 2022/4/27
    */
        boolean result = false;
        try {
            // 初始化FTP客户端
            if (!initFTPClient()) return result;
            // 更换字符集
            filepath = new String(filepath.getBytes("GBK"), "iso-8859-1");
            filename = new String(filename.getBytes("GBK"), "iso-8859-1");
            // 切换目录
            ftp.enterLocalPassiveMode();
            if (!ftp.changeWorkingDirectory(BASEDIR + filepath)) {  //目录不存在，创建目录
                String[] dirs = filepath.split("/");
                String temp = BASEDIR;
                for (String dir : dirs) {
                    if (dir == null) {
                        continue;
                    }
                    temp += "/" + dir;
                    if (!ftp.changeWorkingDirectory(temp)) {
                        if (!ftp.makeDirectory(temp)) return result;  // 创建目录失败
                        else ftp.changeWorkingDirectory(temp); // 创建后重新进入目录
                    }
                }
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            {
                if(!ftp.storeFile(filename,instream))return result;  // 上传失败
            }
            // 传输完成，关闭连接
            instream.close();
            ftp.logout();
            result=true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(ftp.isConnected()){  // 未成功关闭连接，则再次关闭
                try{
                    ftp.disconnect();
                }
                catch (IOException f){
                    f.printStackTrace();
                }
            }
        }
        return result;
    }



    public static boolean downloadFile(String remote,String local,String filename){
        /**
        * @Description:  下载文件到指定目录
        * @Param: [remote, local, filename]
        * @return: boolean
        * @Date: 2022/4/27
        */
        boolean result=false;
        try {
            // 初始化FTP
            if (!initFTPClient()) return result;
            remote = new String(remote.getBytes("GBK"), "iso-8859-1");
            filename = new String(filename.getBytes("GBK"), "iso-8859-1");
            ftp.changeWorkingDirectory(remote); // 切换目录
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            for(FTPFile f:fs){
                if(f.getName().equals(filename)){   // 依次对比找到指定文件
                    ftp.enterLocalPassiveMode();
                    // 创建输出流，传输文件
                    FileOutputStream outstream=new FileOutputStream(new File(local));
                    ftp.retrieveFile(remote+"/"+filename,outstream);
                    result=true;
                    outstream.close();
                }
            }
            ftp.logout();
        }
        catch (IOException e){
            e.printStackTrace();
        }finally {
            if(ftp.isConnected()){
                try {
                    ftp.disconnect();
                }
                catch (IOException f){
                    f.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean downloadFile(String remote,String filename,OutputStream outstream){
        /**
        * @Description:  下载文件到指定的输出流
        * @Param: [remote, filename, outstream]
        * @return: boolean
        * @Date: 2022/4/27
        */
        boolean result=false;
        try {
            // 初始化FTP
            if (!initFTPClient()) return result;
            remote = new String(remote.getBytes("GBK"), "iso-8859-1");
            filename = new String(filename.getBytes("GBK"), "iso-8859-1");
            ftp.changeWorkingDirectory(remote); // 切换目录
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            for(FTPFile f:fs){
                if(f.getName().equals(filename)){   // 依次对比找到指定文件
                    ftp.enterLocalPassiveMode();
                    ftp.retrieveFile(remote+"/"+filename,outstream);
                    result=true;
                }
            }
            ftp.logout();
        }
        catch (IOException e){
            e.printStackTrace();
        }finally {
            if(ftp.isConnected()){
                try {
                    ftp.disconnect();
                }
                catch (IOException f){
                    f.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean deleteFile(String remote,String filename) {
        /**
         * @Description: 删除指定的文件
         * @Param: [remote, filename]
         * @return: boolean
         * @Date: 2022/4/27
         */
        boolean result = false;
        try {
            if (!initFTPClient()) return result;
            filename = new String(filename.getBytes("GBK"), "iso-8859-1");
            remote = new String(remote.getBytes("GBK"), "iso-8859-1");
            ftp.changeWorkingDirectory(remote); // 切换目录
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile f : fs) {
                if (filename.equals("")) return result;   // 删除文件名不能为空
                if (f.getName().equals(filename)) {   // 依次对比、找到了指定文件
                    String filepath = remote + filename;
                    ftp.deleteFile(filepath);
                    result = true;
                }
            }
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    f.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean deleteFolder(String remote) {
        /**
         * @Description: 删除指定的文件夹
         * @Param: [remote]
         * @return: boolean
         * @Date: 2022/4/27
         */
        boolean result = false;
        try {
            if (!initFTPClient()) return result;
            remote = new String(remote.getBytes("GBK"), "iso-8859-1");
            ftp.changeWorkingDirectory(remote); // 切换目录
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            if(fs.length==0) {ftp.removeDirectory(remote);result=true;}   //文件夹为空时，允许删除
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    f.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean renameFile(String oldname,String newname) {
        /**
         * @Description: 重命名文件或文件夹
         * @Param: [newname, oldname]
         * @return: boolean
         * @Date: 2022/4/27
         */
        boolean result = false;
        try {
            if (!initFTPClient()) return result;
            newname = new String(newname.getBytes("GBK"), "iso-8859-1");
            oldname = new String(oldname.getBytes("GBK"), "iso-8859-1");
            ftp.enterLocalPassiveMode();
            ftp.rename(oldname,newname);
            result=true;
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    f.printStackTrace();
                }
            }
        }
        return result;
    }


}


