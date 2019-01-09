package com.mmall.vo;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {
    private List<CartProductVo> cartProductVoList;
    private String imageHost;
    private boolean allCheck;
    private BigDecimal cartTotalPrice;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public boolean isAllCheck() {
        return allCheck;
    }

    public void setAllCheck(boolean allCheck) {
        this.allCheck = allCheck;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
