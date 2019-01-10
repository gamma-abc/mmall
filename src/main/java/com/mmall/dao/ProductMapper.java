package com.mmall.dao;

import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> productSearchByNameAndId(@Param("productName") String productName,@Param("productId") Integer productId);


    List<Product> selectByNameAndProductId(@Param("productName") String productName,@Param("productId") Integer productId);
    // 前端通过关键字和分类id搜索
    List<Product> selectByNameAndCategoryIds(@Param("keyWord") String keyWord, @Param("categoryIdList") List<Integer> categoryIdList);
}