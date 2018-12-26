package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private static final Logger logger=LoggerFactory.getLogger(FTPUtil.class);

    // 1：从配置文件中获取ftp配置信息
    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser= PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass= PropertiesUtil.getProperty("ftp.pass");

    // 2：定义ftp相关参数
    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    // 对外开放的方法，返回上传成功还是失败
    public static boolean upLoadFile(List<File> fileList) throws IOException {      //用集合，读取多个文件
        // 1：创建FTPUtil对象，将配置文件信息赋值给对象
        FTPUtil ftpUtil=new FTPUtil(ftpIp,21,ftpUser,ftpPass);
        // 2：调用私有方法上传文件
        logger.info("开始上传文件");
        boolean result=ftpUtil.upLoadfile("img",fileList);
        logger.info("开始连接ftp服务器,结束上传,上传结果:{}");
        return result;
    }



    // 私有连接服务器方法
    private boolean upLoadfile(String remotePath,List<File> fileList) throws IOException {
        // 1：创建输入流对象,创建Boolean返回值
        FileInputStream fis=null;
        boolean upload=true;
        // 2：开始连接ftp服务器
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                // 更改目录
                ftpClient.changeWorkingDirectory(remotePath);
                // 设置缓存
                ftpClient.setBufferSize(1024);
                // 设置字符集
                ftpClient.setControlEncoding("utf-8");
                // 设置文件类型为二进制类型
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                // 开始传输文件
                for (File file : fileList) {
                    fis=new FileInputStream(file);
                    ftpClient.storeFile(remotePath,fis);
                }

            } catch (IOException e) {
                e.printStackTrace();
                upload=false;
            }finally {
                // 释放资源
                fis.close();
                ftpClient.disconnect();
            }
        }
        return upload;
    }



    // 连接ftp服务器
    private boolean connectServer(String ip,int port,String user,String pwd){
        boolean isSuccess=false;
        FTPClient ftpClient=new FTPClient();
        try {
            ftpClient.connect(ip,port);
            isSuccess=ftpClient.login(user,pwd);
        } catch (IOException e) {
            logger.error("连接ftp服务器失败"+e);
        }
        return isSuccess;
    }





    //构造函数
    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }




    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
