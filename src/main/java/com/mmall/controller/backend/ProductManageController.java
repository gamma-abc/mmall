package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
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
            // 保存业务的操作
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
            // 保存业务的操作
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
            // 保存业务的操作
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
            // 保存业务的操作
            return iProductService.productSearch(productName,productId,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }
    public ServerResponse upload(MultipartFile multipartFile, HttpServletRequest request){
        // 1:获取当前项目路径
        String realPath=request.getSession().getServletContext().getRealPath("upload");
        return null;
    }

}
