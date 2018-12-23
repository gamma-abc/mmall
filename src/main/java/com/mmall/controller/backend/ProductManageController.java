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
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @param productStatus
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
            return iProductService.SetSaleStatus();
        }
        return ServerResponse.createByErrorMessage("需要管理员权限");
    }

}
