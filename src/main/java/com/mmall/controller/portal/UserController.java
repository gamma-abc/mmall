package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录接口
     * @param username
     * @param password
     * @param sesson
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession sesson){
        ServerResponse<User> response= iUserService.login(username,password);
        if (response.isSuccess()){
            sesson.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
     @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }
    @RequestMapping(value = "register.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }
    @RequestMapping(value = "checkvalid.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str ,String type){
        return iUserService.checkValid(str,type);
    }

    /**
     * 获取用户信息
     * 关键：判断User是否为空
     * @param session
     * @return
     */
    @RequestMapping(value = "getUserInfo.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return ServerResponse.createBySuccess(user);
        }
        else {
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
    }

    /**
     * 通过用户名返回用户找回密码的问题
     * @param username
     * @return
     */
    @RequestMapping(value = "forgetGetQuestion.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetUserQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     * 验证问题答案。
     * 难点：token设置的相关知识
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forgetCheckAnswer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
            return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 重置密码
     * @param username
     * @param newPassword
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forgetRestPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username,String newPassword,String forgetToken){
        return iUserService.forgetRestPassword(username,newPassword,forgetToken);
    }

    /**
     * 在登录状态通过旧密码更改密码
     * 1：判断通过session中获取的User是否为空
     * @param session
     * @param OldPassword
     * @param NewPassword
     * @return
     */
    @RequestMapping(value = "restPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> restPassword(HttpSession session,String OldPassword,String NewPassword){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (null==user){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.restPassword(user,OldPassword,NewPassword);
    }
    @RequestMapping(value = "updateinfomation.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfomation(HttpSession session,User user){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
        if (null == currentUser) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> serverResponse=iUserService.updateInformation(user);
        if (serverResponse.isSuccess()) {
            serverResponse.getData().setUsername(Const.CURRENT_USER);
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }

}
