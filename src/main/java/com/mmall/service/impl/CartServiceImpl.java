package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVo=this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 往购物车中添加商品
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse addCart(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            //该商品未添加到购物车
            Cart cartitem = new Cart();
            cartitem.setQuantity(count);
            cartitem.setUserId(userId);
            cartitem.setProductId(productId);
            cartitem.setChecked(Const.Cart.CHECKED);
            int rowCount = cartMapper.insert(cartitem);
            if (rowCount == 0) {
                return ServerResponse.createByErrorMessage("商品添加购物车失败");
            }
        } else {
            //该商品已经在购物车中
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            int rowCount = cartMapper.updateByPrimaryKeySelective(cart);
            if (rowCount == 0) {
                return ServerResponse.createByErrorMessage("商品添加购物车失败");
            }
        }
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 更新购物车产品数量
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //1：查询购物车
        Cart cart=cartMapper.selectByPrimaryKey(userId);
        //2：更新购物车产品数量
        cart.setQuantity(count);
        cartMapper.updateByPrimaryKeySelective(cart);
        //3：重新计算总价
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 删除购物车产品，可以传多个产品id，删除多个商品
     * @param userId
     * @param productIds
     * @return
     */
    @Override
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds) {
        if (productIds == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //1：将前端传过来的多个产品id组装成一个集合，用于在数据库中批量删除
        List<String> productList= Splitter.on(",").splitToList(productIds);
        //2：调用数据库删除购物车中的产品
        cartMapper.deleteByUserIdProducts(userId,productList);
        //3：删除之后重新查询购物车产品的数量，计算总价
        CartVo cartVo=this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 封装购物车抽象类的方法，并计算购物车中所有商品的总价
     *
     * @param userId
     * @return
     */
    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        //1：通过UserId从数据库中查询购物车的产品信息
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        //2：创建购物车产品的VO视图
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        //3：价格的计算
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setUserId(cartItem.getUserId());
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());
                    //判断商品库存
                    int buyLimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        //库存充足
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        //库存不足
                        buyLimitCount = product.getStatus();
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算当前产品的总价
                    cartProductVo.setProductPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(Const.Cart.CHECKED);
                }
                //如果是勾选状态，将当前商品加入到总价中
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductPrice().doubleValue());
                }
                //将当前产品的ProductVo添加到ProductVoList中
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setAllCheck(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getproperitys("ftp.server.http.prefix"));
        return cartVo;
    }

    /**
     * 判断购物车是否是全选状态
     *
     * @param userId 通过userId查询数据库中checked字段的值
     * @return
     */
    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }

    /**
     * 全选，全不选，根据productId反选
     * @param userId
     * @param productId
     * @param checked
     * @return
     */
    @Override
    public ServerResponse selectOrUnselectAll(Integer userId, Integer productId,int checked) {
        cartMapper.checkOrUncheckProduct(userId,null,checked);
        return this.list(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if (userId == null) {
            return ServerResponse.createBySuccessMessage("0");
        }
        return ServerResponse.createBySuccess(cartMapper.getCartProductCount(userId));
    }
}
