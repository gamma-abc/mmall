package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;

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
    public ServerResponse<Object> manageProductDetail(Integer productId){
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品不存在或已经删除");
        }
        // VO对象
    }
    private assemblageProductDetailVo(Product product){
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
    }
}
