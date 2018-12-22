package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.common.ServerResponseByFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping(value = "/update/")
public class UpdateController {

    @RequestMapping(value = "he.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponseByFile updateImg(String file, String filename, MultipartFile multipartFile,
                                            HttpServletRequest request){
        String [] img={"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1545217420020&di=01b0a80c" +
                "73dcbe433cc8786957793f27&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F960a304e" +
                "251f95caf1852c0bc4177f3e6709521e.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1545217420019&di=b1c2645db65170d25b211" +
                        "59aeeb02674&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F4b90f603738da9775" +
                        "3662396bd51f8198718e3c6.jpg"};
        try {
            // 获取项目路径
            String realPath = request.getSession().getServletContext()
                    .getRealPath("");
            System.out.println(realPath+file+filename);
            InputStream inputStream = multipartFile.getInputStream();
            String contextPath = request.getContextPath();
            // 服务器根目录的路径
            String path = realPath.replace(contextPath.substring(1), "");
            // 根目录下新建文件夹upload，存放上传图片
            String uploadPath = path + "upload";
            // 获取文件名称

            // 将文件上传的服务器根目录下的upload文件夹
            File file1 = new File(uploadPath, filename);
            FileUtils.copyInputStreamToFile(inputStream, file1);
            // 返回图片访问路径
            String url = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + "/upload/" + filename;

            String [] str = {url};

            return ServerResponseByFile.createBySuccess(str);
        } catch (IOException e) {
            System.out.println(e);
        }
        return ServerResponseByFile.createBySuccess(img);
    }
}
