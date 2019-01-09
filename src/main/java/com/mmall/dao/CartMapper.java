package com.mmall.dao;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    // 根据用户id查询该用户的购物车
    Cart selectCartByUserIdProductId(@Param("userId") Integer userId,@Param("productId") Integer productId);
    //根据用户userId查询购物车集合
    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProducts(@Param("userId") Integer userId, @Param("productList") List<String> productList);

    int checkOrUncheckProduct(@Param("userId") Integer userId, @Param("productId") Integer productId,@Param("check") int checked);

    int getCartProductCount(Integer userId);
}