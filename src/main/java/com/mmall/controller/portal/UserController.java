package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/")
public class UserController {

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    public ServerResponse<User> login(){
        ServerResponse<User> response;

        return response;
    }
}
