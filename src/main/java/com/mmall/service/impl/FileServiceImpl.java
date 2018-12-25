package com.mmall.service.impl;

import com.mmall.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

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
        return null;
    }

}
