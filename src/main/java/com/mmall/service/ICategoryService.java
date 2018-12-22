package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.List;

public interface ICategoryService {
    ServerResponse addCatgory(String categoryName, Integer parentId);
    ServerResponse updateCategory(Integer cateotryId,String categoryName);
    ServerResponse getChildParallelCategory(Integer category);
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
