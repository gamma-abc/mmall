package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.common.ServerResponseByFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/update/",method = RequestMethod.POST)
public class UpdateController {
    @RequestMapping("he.do")
    @ResponseBody
    public ServerResponseByFile updateImg(){
        Object [] img={"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1545217420020&di=01b0a80c" +
                "73dcbe433cc8786957793f27&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F960a304e" +
                "251f95caf1852c0bc4177f3e6709521e.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1545217420019&di=b1c2645db65170d25b211" +
                        "59aeeb02674&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F4b90f603738da9775" +
                        "3662396bd51f8198718e3c6.jpg"};
        return ServerResponseByFile.createBySuccess(img);
    }
}
