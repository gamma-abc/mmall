package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

import java.util.Map;

public interface IShippingService {
    ServerResponse add(Integer id, Shipping shipping);

    ServerResponse<String> del(Integer id, Integer shippingId);

    ServerResponse<String> update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> getShipping(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Integer userId);
}
