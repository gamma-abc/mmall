package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;
import org.springframework.stereotype.Service;


public interface ICartService {
    ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    ServerResponse<CartVo> list(Integer id);

    ServerResponse selectOrUnselectAll(Integer id,Integer productId, int checked);

    ServerResponse<Integer> getCartProductCount(Integer id);
}
