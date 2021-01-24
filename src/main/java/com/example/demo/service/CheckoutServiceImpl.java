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
    private static final BigDecimal DISCOUNTED_PRICE_CARD_HOLDERS   = new BigDecimal(8.25);
    private static final BigDecimal PRICE_DISCOUNT_THRESHOLD        = BigDecimal.valueOf(60);
    private static final int        DISCOUNT_THRESHOLD_CARD_HOLDERS = 2;
    private static final int CARD_HOLDERS_ID                 = 1;
    private static final BigDecimal CARD_HOLDERS_DISCOUNT = new BigDecimal(0.75);


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
        //Total price threshold (e.g. £60) discount
        Cart priceThresholdDiscountedCart = getPriceThresholdCart(cart);
        //Quantity threshold discount
        Cart quantityThresholdDiscountedCart = getQuantityThresholdCart(cart);
        //FUTURE--->Bundle of different products threshold dicount
        //*************************************


        /*  STEP 3: CHOOSE ONE CART                         */
        BigDecimal bestTotalPrice = cart.getTotal().min(quantityThresholdDiscountedCart.getTotal())
                                                 .min(priceThresholdDiscountedCart.getTotal());
        if(bestTotalPrice.equals(cart.getTotal())) {
            for (int i = 0; i < cart.getItems().size(); i++) {
                cart.getItems().get(i).setDiscount(BigDecimal.ZERO);
            }
            return cart;
        }
        if (bestTotalPrice.equals(priceThresholdDiscountedCart.getTotal())) {
            priceThresholdDiscountedCart.setItems(updateIndividualDiscountField(priceThresholdDiscountedCart));
            return priceThresholdDiscountedCart;
        }
        return quantityThresholdDiscountedCart;
    }

    private Cart getQuantityThresholdCart(Cart cart) {
        Cart quantityThresholdCart = new Cart();
        quantityThresholdCart.setInitialTotalValue(cart.getInitialTotalValue());
        quantityThresholdCart.setTotal(cart.getTotal());
        quantityThresholdCart.setItems(cart.getItems());
        quantityThresholdCart.setId(cart.getId());
        quantityThresholdCart.setUser(cart.getUser());
        quantityThresholdCart.setMessage("Quantity based discount is applied");

        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal cnt = new BigDecimal(0);
        List<Long> idsChekcked = new ArrayList<>();
        final var items = quantityThresholdCart.getItems();
        final var size = items.size();
        for (int i = 0; i < size; i++) {
            if (idsChekcked.contains(items.get(i).getId()) ||
                items.get(i).getQuantityDiscountValue().equals(BigDecimal.ZERO)) {
                continue;
            }
            cnt = cnt.add(BigDecimal.valueOf(1));
            final var quantityDiscountThreshold = items.get(i).getQuantityDiscountThreshold();
            if (cnt.compareTo(quantityDiscountThreshold) == 0) {
                for (int k = 0; k < size; k++) {
                    if (items.get(k).getId() == items.get(i).getId()) {
                        final var item = items.get(k);
                        item.setDiscount(item.getPrice().subtract(item.getQuantityDiscountValue()));
                        final BigDecimal discount = item.getDiscount();//items.get(k).getPrice().subtract(items.get(k).getQuantityDiscountValue());
                        totalDiscount = totalDiscount.add(discount);
                    }
                }
                idsChekcked.add(items.get(i).getId());
            }

        }
        quantityThresholdCart.setTotal(cart.getTotal().subtract(totalDiscount));
        quantityThresholdCart.setTotalDiscount(totalDiscount);
        return quantityThresholdCart;
    }

    private List<Item> updateIndividualDiscountField(Cart cart) {
        List<Item> items = cart.getItems();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setDiscount(getDiscountedPrice(items.get(i).getPrice(), DISCOUNT));
        }
        return items;
    }


    private void finishInitialCartSetup(Cart cart) {
        cart.setTotal(cart.getInitialTotalValue());
        cart.setTotalDiscount(BigDecimal.ZERO);
        cart.setMessage("No discount applied");
    }


    private Cart getPriceThresholdCart(Cart cart) {
        BigDecimal initialTotalValue = cart.getInitialTotalValue();
        if (initialTotalValue.compareTo(PRICE_DISCOUNT_THRESHOLD) > 0) {
            Cart priceThresholdCart = new Cart();
            priceThresholdCart.setInitialTotalValue(cart.getInitialTotalValue());
            priceThresholdCart.setItems(cart.getItems());
            priceThresholdCart.setId(cart.getId());
            priceThresholdCart.setUser(cart.getUser());
//            priceThresholdCart.setMessage(cart.getMessage());
            priceThresholdCart.setTotal(getDiscountedPrice(initialTotalValue, DISCOUNT));
            priceThresholdCart.setTotalDiscount(initialTotalValue.subtract(priceThresholdCart.getTotal()));
            priceThresholdCart.setMessage(DISCOUNT + "% discount is applied for purchases that are above " + PRICE_DISCOUNT_THRESHOLD);
            return priceThresholdCart;
        }
        return cart;
    }


    private void applyTotalPriceDiscount(Cart cart) {

    }

    private void applyQuantityDiscount(Cart cart) {

    }

    private BigDecimal getDiscountedPrice(BigDecimal initialValue, BigDecimal discountPercentage) {
        return initialValue.subtract(initialValue.multiply(discountPercentage.divide(ONE_HUNDRED)));
    }

}