package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 更新或保存产品
     * @param product
     * @return
     */
    public ServerResponse SavaOrUpdateProduct(Product product){
        // 1:判断Product是否为空
        if (product != null) {
            // 2:判断子图是否为空，如果不为空就把第一个子图赋值给主图
            if (StringUtils.isNotBlank(product.getSubImages())){
                // 子图在数据库中存储的格式，以 ，分割
                // 241997c4-9e62-4824-b7f0-7425c3c28917.jpeg,
                // b6c56eb0-1748-49a9-98dc-bcc4b9788a54.jpeg,
                // 92f17532-1527-4563-aa1d-ed01baa0f7b2.jpeg
                // 把子图用 逗号 分割，存在数组中，
                String [] SubImage=product.getSubImages().split(",");

                // 判断是否存在子图
                if (SubImage.length > 0) {
                    // 如果存在子图则把第一个子图赋值给主图
                    product.setMainImage(SubImage[0]);
                }
                // 如果是更新信息，则id一定不为空，以此来判断是更新产品，还是保存产品
                if (product.getId() != null) {
                    int rowCount=productMapper.updateByPrimaryKey(product);
                    if (rowCount > 0) {
                        return ServerResponse.createBySuccessMessage("更新产品信息成功");
                    }else {
                        return ServerResponse.createByErrorMessage("更新产品信息失败");
                    }
                }
                int rowCount=productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("保存产品信息成功");
                }else{
                    return ServerResponse.createByErrorMessage("保存产品信息失败");
                }
            }
        }
        return ServerResponse.createByErrorMessage("保存或更新产品失败");
    }

    /**
     * 设置商品上下架
     * @param productId
     * @param productStatus
     * @return
     */
    public ServerResponse<String> SetSaleStatus(Integer productId,Integer productStatus){
        // 判断参数是否为空
        if (productId == null || productStatus == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=new Product();
        product.setId(productId);
        product.setStatus(productStatus);
        int rowCount=productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("操作成功");
        }
        return ServerResponse.createByErrorMessage("设置商品上下架失败");
    }

    /**
     * 获取商品的详情信息
     * @param productId
     * @return
     */
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品不存在或已经删除");
        }
        // VO对象
        ProductDetailVo productDetailVo=assemblageProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /**
     * 获取VO对象的方法
     * @param product
     * @return ProductDetailVo对象
     */
    private ProductDetailVo assemblageProductDetailVo(Product product){
        ProductDetailVo productDetailVo=new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        // 从配置文件中加载Image路径的前缀
        productDetailVo.setImageHost(PropertiesUtil.getProperty(""));

        Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0); //节点为空则设置为默认根节点
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        //通过DateTimeUtil工具类设置时间
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    /**
     * 分页,使用Mybatis分页插件
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize){
        // 1:startPage --start
        // 2:填充自己的SQL查询逻辑
        // 3：pageHelper --收尾
        PageHelper.startPage(pageNum,pageSize);
        // 1：把从数据库查询的Product对象存放在List集合中
        List<Product> productList=productMapper.selectList();
        // 2：创建一个ProductListVo对象的集合，
        List<ProductListVo> productListVoList= Lists.newArrayList();
        for (Product product : productList) {
            // 通过assembleProductListVo方法获取ProductListVo，添加到ProdutListVo的集合中
            ProductListVo productListVo=assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult=new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * ProductListVo对象的组装方法
     * @param product
     * @return
     */
    public ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo=new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setcategoryId(product.getCategoryId());
        productListVo.setSubTitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    /**
     * 后台产品搜索
     * @param productName
     * @param productId
     * @return
     */
    public ServerResponse<PageInfo> productSearch(String productName, Integer productId,Integer pageNum, Integer pageSize){
        // 1：startPage --开始
        PageHelper.startPage(pageNum,pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName=new StringBuilder().append("%").append(productName.trim()).append("%").toString();
        }
        // 1：从数据库中查到product，赋值给集合
        List<Product> productList=productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVoList=Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo=assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo=new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 前台商品详情页
     * 1:判断从后台查询的商品是否为空
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createByErrorMessage("商品不存在或已删除");
        }
        ProductDetailVo productDetailVo=assemblageProductDetailVo(product);
        if (productDetailVo.getStatus() == Const.productStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("商品已下架");
        }
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /**
     * 通过关键字或分类ID查询商品信息
     * @param keyWord
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServerResponse<PageInfo> getProductByKeywordAndCategoryId(String keyWord, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        // 1：判断是否存在keyword和categoryId
        if (StringUtils.isEmpty(keyWord) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        // 2:从数据库中查询是否存在该分类id
        if (categoryId != null) {
            Category category=categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null) {

            }
        }
        return null;
    }
}
