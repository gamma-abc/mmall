package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    private Logger logger=LoggerFactory.getLogger(CategoryServiceImpl.class);
    /**
     * 增加节点
     * 1：判断节点是否为空，parentId是否为空
     * 2：调用set方式设置category的属性
     * 3：调用CategoryMapper增加节点
     * @param categoryName
     * @param parentId
     * @return
     */
    public ServerResponse addCatgory(String categoryName,Integer parentId){
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        //设置品类属性
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int checkCategoryname=categoryMapper.selectCategoryByCategoryName(categoryName);
        if (checkCategoryname>0){
            return ServerResponse.createByErrorMessage("节点名称重复，添加失败");
        }
        int insertCount=categoryMapper.insert(category);
        if (insertCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    /**
     * 更新category Name
     * 1:判断category ID是否为空，categoryName是否为空
     * 2:创建category对象，并设置id和name
     * @param cateotryId
     * @return
     */
    public ServerResponse updateCategory(Integer cateotryId,String categoryName){
        if (cateotryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category =new Category();
        category.setId(cateotryId);
        category.setName(categoryName);
        int resultCount=categoryMapper.updateByPrimaryKeySelective(category);
        if (resultCount > 1) {
            return ServerResponse.createBySuccessMessage("更新品类名称成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名称失败");
    }

    /**
     * 获取平级的字节的
     * 1:判断categoryId是否为空
     * 2：用List集合存储返回值
     * @param parentId
     * @return
     */
    public ServerResponse<List<Category>> getChildParallelCategory(Integer parentId){
        if (parentId == null) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(parentId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归查询本节点id和孩子节点id
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        // 1：初始化Set
        Set<Category> categorySet= Sets.newHashSet();
        // 2:调用下面的递归方法
        findChildCategory(categorySet,categoryId);
        // 3:返回的是包含category的List集合
        List<Integer> categoryIdList= Lists.newArrayList();
        // 4：判断是否为空
        if (categoryIdList != null) {
            for (Category categoryItem:categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    /**
     * 递归算法，查找子节点
     * 1：在数据库中查找当前节点的category对象
     * @param categoryId
     * @return
     */
    public Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId){
        // 1:在数据库中查找当前节点的category对象
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        // 2：判断对象是否为空，不为空则加入到Set集合中
        if (category != null) {
            categorySet.add(category);
        }
        // 查找子节点，递归算法一定要有一个退出条件
        List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category category1:categoryList){
            findChildCategory(categorySet,category1.getId());
        }
        return categorySet;
    }

}
