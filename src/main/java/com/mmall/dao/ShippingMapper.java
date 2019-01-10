package com.mmall.dao;

import com.mmall.pojo.Product;
import com.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
    //通过userId删除收货地址
    int deleteByUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);
    //通过userId更新收货地址
    int updateByUserId(Shipping shipping);

    Shipping selectByUserIdAndShippingId(@Param("userId") Integer userId, @Param("shipingId") Integer shippingId);

    List<Shipping> selectByUserId(Integer userId);
}