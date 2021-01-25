package com.example.demo.service;


import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class CheckoutServiceImpl implements CheckoutService{

    @Autowired CartRepository cartRepository;
    @Autowired ItemRepository itemRepository;

    private static final BigDecimal DISCOUNT = new BigDecimal(10);
    private static final BigDecimal ONE_HUNDRED                   = new BigDecimal(100);
    private static final BigDecimal PRICE_DISCOUNT_THRESHOLD        = BigDecimal.valueOf(60);


    //Implement discount logic
    @Override public Cart applyCheckoutDetails(Cart cart) {
        return passCartThroughDiscountsFilter(cart);
    }


    private Cart passCartThroughDiscountsFilter(Cart cart) {
        /*  STEP 1: SET UP                                  */
        var initialTotalValue = cart.getInitialTotalValue();
        /*  STEP 2: GET HYPOTHETICAL CARTS                  */
        //Finish setting up the initial cart
        finishInitialCartSetup(cart);
        //Quantity threshold discount
        quantityThresholdFilter(cart);
        //Total price threshold (e.g. £60) discount
        totalPriceThresholdFilter(cart);
        //FUTURE--->Bundle of different products threshold dicount
        //*************************************
        return cart;
    }

    private void quantityThresholdFilter(Cart cart) {

        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal cnt = new BigDecimal(0);
        List<Long> idsChekcked = new ArrayList<>();
        var items = cart.getItems();
        final var size = items.size();
        for (int i = 0; i < size; i++) {
            if (idsChekcked.contains(items.get(i).getId()) ||
                items.get(i).getQuantityDiscountThreshold().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            cnt = cnt.add(BigDecimal.valueOf(1));

            for (int j = i + 1; j < size; j++) {
                if (items.get(j).getId() == items.get(i).getId()) {
                    cnt = cnt.add(BigDecimal.valueOf(1));
                }
            }
            final var quantityDiscountThreshold = items.get(i).getQuantityDiscountThreshold();
            if (cnt.compareTo(quantityDiscountThreshold) >= 0) {
                cart.setMessage("Quantity based discount is applied");
                for (int k = 0; k < size; k++) {
                    if (items.get(k).getId() == items.get(i).getId()) {
                        final var item = items.get(k);
                        item.setDiscount(item.getPrice().subtract(item.getQuantityDiscountValue()));
                        final BigDecimal discount = item.getDiscount();
                        cart.getItems().get(k).setDiscount(discount);
                        totalDiscount = totalDiscount.add(discount);
                    }
                }
                idsChekcked.add(items.get(i).getId());
            }

        }
        cart.setTotal(cart.getTotal().subtract(totalDiscount));
        cart.setTotalDiscount(totalDiscount);
    }

    private void finishInitialCartSetup(Cart cart) {
        cart.setTotal(cart.getInitialTotalValue());
        cart.setTotalDiscount(BigDecimal.ZERO);
        cart.setMessage("No discount applied");
        for (int i = 0; i < cart.getItems().size(); i++) {
            cart.getItems().get(i).setDiscount(BigDecimal.ZERO);
        }
    }


    private void totalPriceThresholdFilter(Cart cart) {
        BigDecimal totalAfterQuantityReductions = cart.getTotal();
        if (totalAfterQuantityReductions.compareTo(PRICE_DISCOUNT_THRESHOLD) > 0) {
            cart.setTotal(getDiscountedPrice(totalAfterQuantityReductions, DISCOUNT));
            cart.setTotalDiscount(cart.getInitialTotalValue().subtract(cart.getTotal()));
            cart.setMessage(cart.getMessage() + " and " + DISCOUNT + "% discount is applied because the order is above " + PRICE_DISCOUNT_THRESHOLD);
        }
    }

    private BigDecimal getDiscountedPrice(BigDecimal initialValue, BigDecimal discountPercentage) {
        return initialValue.subtract(initialValue.multiply(discountPercentage.divide(ONE_HUNDRED)));
    }

}