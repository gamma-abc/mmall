package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    /**
     * 新增用户地址
     * @param userId
     * @param shipping
     * @return
     */
    @Override
    public ServerResponse add(Integer userId,Shipping shipping){
        //1：将userId添加到shipping对象中
        shipping.setUserId(userId);
        //2：将地址的对象插入到数据库中，同时返回rowCount是地址主键，需在mapper中设置主键回填属性
        //      useGeneratedKeys="ture" keyProterty="id"
        //      参考《Java EE互联网轻量级框架整合开发 SSM框架》 5.33小节，主键回填
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            //3：拼装map返回给前端
            Map<String,Integer> result= Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功",shipping);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> del(Integer userId, Integer shippingId) {
        //1：通过查找userId，将该收货地址删除
        int resultCount = shippingMapper.deleteByUserId(userId, shippingId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    /**
     * 更新用户收货地址
     * @param userId
     * @param shipping
     * @return
     */
    @Override
    public ServerResponse<String> update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int resultCount = shippingMapper.updateByUserId(shipping);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    /**
     * 获取用户收货地址
     * @param userId
     * @param shippingId
     * @return
     */
    @Override
    public ServerResponse<Shipping> getShipping(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("找不到该收货地址");
        }
        return ServerResponse.createBySuccess(shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Integer userId) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList=shippingMapper.selectByUserId(userId);
        PageInfo pageInfo=new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
