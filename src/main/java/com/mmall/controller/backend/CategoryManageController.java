package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;
    /**
     * 增加节点
     * 1：判断session中的用户是否为空
     * 2：判断是否为管理员
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("addcategory.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,
                                      @RequestParam(value = "parentId",defaultValue = "0") int parentId){

        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        //判断是否是管理员
        if (iUserService.checkAdmin(user).isSuccess()) {

            return iCategoryService.addCatgory(categoryName,parentId);
        }
        return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
    }

    /**
     * 更新category Name
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping("setcategoryname.do")
    @ResponseBody
    public ServerResponse serCategoryName(HttpSession session ,Integer categoryId,String categoryName){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        //判断是否是管理员
        if (iUserService.checkAdmin(user).isSuccess()) {
                return iCategoryService.updateCategory(categoryId,categoryName);
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }
    @RequestMapping("getcategory.do")
    @ResponseBody
    public ServerResponse<List<Category>> getChildParallelCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        //判断是否是管理员
        if (iUserService.checkAdmin(user).isSuccess()) {
            //查询子节点的category信息，平级，不递归子子节点
            return iCategoryService.getChildParallelCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }
    @RequestMapping("getchildcategory.do")
    @ResponseBody
    public ServerResponse getChildDeepAndChildrenCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        //判断是否是管理员
        if (iUserService.checkAdmin(user).isSuccess()) {
            //查询当前id，递归子节点id
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }

}
