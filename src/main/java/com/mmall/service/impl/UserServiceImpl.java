package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount=userMapper.checkUsername(username);
        if (resultCount==0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        // MD5加密
        String MD5Password= MD5Util.MD5EncodeUtf8(password);
        User user=userMapper.selectLonin(username,MD5Password);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }
        // 把密码,提示问题，问题答案都设置为空
        user.setPassword(StringUtils.EMPTY);
        user.setAnswer(StringUtils.EMPTY);
        user.setQuestion(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功",user);
    }
    public ServerResponse<String> register(User user){
        ServerResponse validResponse=this.checkValid(user.getUsername(),Const.USERNAEM);
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse=this.checkValid(user.getEmail(),Const.EMALL);
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        // 设置用户权限
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount=userMapper.insert(user);
        if (resultCount==0){
            return ServerResponse.createByErrorMessage("用户注册失败");
        }
        return ServerResponse.createBySuccessMessage("用户注册成功");
    }

    /**
     * 校验用户名或邮箱是否可用（数据库不存在）
     * 1：判断用户输入的是字符串还是邮箱
     * 2：在数据库查找是否存在，查询不到则校验成功，
     *      否则返回用户名已存在
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str,String type){
        if (org.apache.commons.lang3.StringUtils.isNotBlank(str)){
            if (Const.USERNAEM.equals(type)){
                int resultCount=userMapper.checkUsername(str);
                if (resultCount>0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMALL.equals(type)){
                int resultCount=userMapper.checkEmail(str);
                if (resultCount>0){
                    return ServerResponse.createByErrorMessage("Email已存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 查询用户的密码提示问题
     * 1:校验用户名是否存在
     * @param username
     * @return
     */
    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> ValidUsername=this.checkValid(username,Const.USERNAEM);
        if (ValidUsername.isSuccess()){
            //返回成功--用户名不存在
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String UserQuestion=userMapper.SelectQuestionByUsername(username);
        if (StringUtils.isNotBlank(UserQuestion)){
            return ServerResponse.createBySuccess(UserQuestion);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int result=userMapper.checkAnswer(username,question,answer);
        if (result>0){
            //说明问题和答案是这个用户
            //创建token
            String forgetTocen= UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetTocen);
            return ServerResponse.createBySuccess(forgetTocen);
        }else {
            return ServerResponse.createByErrorMessage("找回密码问题错误");
        }
    }
    /**
     * 1:校验用户名是否存在
     * 2：验证客户端传来的touken是否为空
     * 2:通过用户名获取当前用户的token
     * 3:判断当前token是否为空，为空则该用户登录的登录touken已经过期
     * 4:当前获取的token和客户端传过来的touken进行对比，使用StringUnit.equals();方法比较
     * 5:当比较相等时，在if中调用更新密码的方法
     * @param username
     * @param newPassword
     * @param forgetToken
     * @return
     */
    @Override
    public ServerResponse<String> forgetRestPassword(String username, String newPassword, String forgetToken) {
        ServerResponse validusername=this.checkValid(username,Const.USERNAEM);  //判断用户是否存在
        if (validusername.isSuccess()){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("touken需要被传递");      //判断客户端传过来的touken是否为空
        }
        String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token已经过期");          //判断工具类生成的touken是否为空
        }                                                                    // 为空则说明该用户的touken已经过期
        if (StringUtils.equals(token,forgetToken)){
            String MD5NewPassword=MD5Util.MD5EncodeUtf8(newPassword);
            int result=userMapper.updatePasswordByUsername(username,MD5NewPassword);
            if (result>0){
                return ServerResponse.createBySuccessMessage("密码更新成功");
            }
        }else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取token");
        }
        return ServerResponse.createByErrorMessage("修改密码错误");
    }

    /**通过旧密码更改密码
     * 1:判断用户是否存在
     * 2:为防止横向越权，要校验一下该用户的旧密码
     * @param user
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Override
    public ServerResponse<String> restPassword(User user, String oldPassword, String newPassword) {
        ServerResponse validUsername=this.checkValid(user.getUsername(),Const.USERNAEM);
        if (validUsername.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        int result=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
        if (result==0){
            return ServerResponse.createByErrorMessage("旧密码输入错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount=userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更改失败");
    }

    /**更新用户信息
     * 1:用户名不能被更新
     * 2：验证新的邮箱是否已存在，如果存在相同的邮箱，验证是否是当前用户的邮箱
     * @param user
     * @return
     */
    @Override
    public ServerResponse<User> updateInformation(User user) {
        int checkEmailResult=userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (checkEmailResult > 0) {
            return ServerResponse.createByErrorMessage("Email已存在，请更换Email");
        }
        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setQuestion(user.getQuestion());
        int updateCount=userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("更新用户信息成功");
        }
        return ServerResponse.createByErrorMessage("更新用户信息失败");
    }

    /**
     * 获取当前用户信息
     * 1：通过userId从数据库中获取用户信息赋值给User对象
     * 2：获取到用户信息后将密码设置为空，之后将user对象返回给Controller层
     * @param id
     * @return
     */
    @Override
    public ServerResponse<User> getInformation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (null == user) {
            return ServerResponse.createByErrorMessage("找不到当前用户的信息");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 判断是否是管理员
     * @param user
     * @return
     */
    public ServerResponse<User> checkAdmin(User user){
        if (user!=null&&user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();

    }
}
