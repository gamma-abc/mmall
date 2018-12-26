package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;
    /**
     * 保存商品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productsave(HttpSession session, Product product){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        // 1：判断用户是否登录
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，需要登录");
        }
        // 2：判断管理员权限
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            // 保存业务的操作
            return iProductService.SavaOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }
    /**
     * 设置产品上下架状态
     * @param session
     * @param productId
     * @param productStatus
     * @return
     */
    @RequestMapping("setsalestatus.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer productStatus){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        // 1：判断用户是否登录
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，需要登录");
        }
        // 2：判断管理员权限
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            // 业务的操作
            return iProductService.SetSaleStatus(productId,productStatus);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }
    /**
     * 获取产品详情信息
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        // 1：判断用户是否登录
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，需要登录");
        }
        // 2：判断管理员权限
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            // 业务的操作
            return iProductService.manageProductDetail(productId);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 利用Mybatis分页插件  分页product查询结果
     * 难点：productListVo
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        // 1：判断用户是否登录
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，需要登录");
        }
        // 2：判断管理员权限
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            // 业务的操作
            return iProductService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        // 1：判断用户是否登录
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，需要登录");
        }
        // 2：判断管理员权限
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            // 业务的操作
            return iProductService.productSearch(productName,productId,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

    /**
     * 图片上传
     * 1:要判断管理员权限
     * 2：FTPUtil工具类，涉及到较多知识点
     * @param multipartFile
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,MultipartFile multipartFile, HttpServletRequest request){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        // 1：判断用户是否登录
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，需要登录");
        }
        // 2：判断管理员权限
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            // 1:获取当前项目路径
            String realPath=request.getSession().getServletContext().getRealPath("upload");
            // 2:调用Service层存储图片,返回的是存储在服务器上的文件名
            String targetFileName=iFileService.upload(multipartFile,realPath);
            // 3:通过配置文件的ftp服务器前缀+文件名组成外部可访问的URL
            String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            // 通过Map组装成符合前端要求的格式
            Map map= Maps.newHashMap();
            map.put("uri",targetFileName);
            map.put("url",url);
            return ServerResponse.createBySuccess(map);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }
}
