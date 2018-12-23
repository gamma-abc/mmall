package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

public interface IProductService {
    // 保存或更新产品
    ServerResponse SavaOrUpdateProduct(Product product);
    // 设置商品上下架
    ServerResponse<String> SetSaleStatus(Integer productId,Integer productStatus);
}
