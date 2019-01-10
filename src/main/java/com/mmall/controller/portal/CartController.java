package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping(value = "add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session,Integer count,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.addCart(user.getId(),productId,count);
    }

    /**
     * 更新购物车产品
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session,Integer count,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(),productId,count);
    }

    /**
     * 删除购物车的产品，可以删除多个产品
     *
     * @param session
     * @param productIds    删除多个产品时该参数格式    product1,product2,product3
     * @return
     */
    @RequestMapping(value = "delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpSession session,String productIds){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }

    /**
     * 购物车全选
     * @param session
     * @return
     */
    @RequestMapping(value = "selectAll")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselectAll(user.getId(),null,Const.Cart.CHECKED);
    }

    /**
     * 全不选
     * @param session
     * @return
     */
    @RequestMapping(value = "unSelectAll")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselectAll(user.getId(),null,Const.Cart.UN_CHECKED);
    }

    /**
     * 根据productId反选
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "unSelectByProductId")
    @ResponseBody
    public ServerResponse<CartVo> unSelectByProduct(HttpSession session,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselectAll(user.getId(),productId,Const.Cart.UN_CHECKED);
    }

    /**
     * 显示购物车的产品数量
     * @param session
     * @return
     */
    public ServerResponse<Integer> getCartProductCount(HttpSession session){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            //如果用户未登录，不能返回错误信息，将购物车数量置为0
            return ServerResponse.createBySuccessMessage("0");
        }
        return iCartService.getCartProductCount(user.getId());
    }
}
