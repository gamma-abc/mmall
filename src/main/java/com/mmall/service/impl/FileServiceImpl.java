package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        // 1:获取文件名
        String fileName=file.getOriginalFilename();
        //substring截取字符串
        // lastIndexOf从行末开始匹配字符串
        // 获取拓展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        // 拼接图片在数据库中的文件名
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
        // 打印日志信息
        logger.info("开始上传文件，文件名是:{},上传的路径是{},新文件名是：{}",fileName,path,uploadFileName);
        File fileDir=new File(path);
        //判断文件夹是否存在
        if (!fileDir.exists()) {
            fileDir.setWritable(true);      //目录有写权限
            fileDir.mkdirs();               //不存在就创建目录
        }
        File targetFile=new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            //文件上传成功
            // 1:将图片上传到Ftp服务器
            FTPUtil.upLoadFile(Lists.newArrayList(targetFile));
            // 2：删除该路径下的图片
            targetFile.delete();

        } catch (IOException e) {
            logger.error("文件上传异常："+e);
            return null;
        }
        // 返回文件名
        //A:abc.jpg
        //B:abc.jpg
        return targetFile.getName();
    }

}
