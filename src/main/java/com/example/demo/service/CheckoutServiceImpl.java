package com.example.demo.service;


import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.hibernate.type.DoubleType.ZERO;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    private static final BigDecimal DISCOUNT = new BigDecimal(10);
    private static final BigDecimal ONE_HUNDRED                   = new BigDecimal(100);
    private static final BigDecimal DISCOUNTED_PRICE_CARD_HOLDERS = new BigDecimal(8.25);
    private static final long       THRESHOLD_VALUE               = 60;
    private static final int DISCOUNT_THRESHOLD_CARD_HOLDERS = 2;
    private static final int CARD_HOLDERS_ID                 = 1;
    private static final BigDecimal CARD_HOLDERS_DISCOUNT = new BigDecimal(0.75);
    @Autowired CartRepository cartRepository;

    //Implement discount logic
    @Override public Cart applyCheckoutDetails(Cart cart) {
        applyDiscounts(cart);

        return cart;
    }

    private void applyDiscounts(Cart cart) {
        final List<Item> items = cart.getItems();
        final BigDecimal total = cart.getTotal();
        BigDecimal currentDiscount = cart.getTotalDiscount();

        //Above £60 discount
        cart.setInitialTotalValue(total);
        cart.setTotalDiscount(BigDecimal.valueOf(ZERO));
        if (total.compareTo(BigDecimal.valueOf(THRESHOLD_VALUE)) > 0) {
            cart.setTotal(total.subtract(total.multiply(DISCOUNT.divide(ONE_HUNDRED))));
            cart.setTotalDiscount(cart.getInitialTotalValue().subtract(cart.getTotal()));
            cart.setMessage("10% discount is applied for purchases that are above £60");
            return;
        }
        //2 travel card holders discount
        int cnt = 0;
        for (Item item : items) {
            if (item.getId() == CARD_HOLDERS_ID) {
                cnt++;
            }
            if (cnt == DISCOUNT_THRESHOLD_CARD_HOLDERS) {
                System.out.println("------------> curr " + currentDiscount);
                for (Item discountedItem : items) {
                    System.out.println("------------> curr0 " + currentDiscount);

                    if (discountedItem.getId() == CARD_HOLDERS_ID) {
                        System.out.println("------------> curr2 " + currentDiscount);

                        discountedItem.setDiscount(CARD_HOLDERS_DISCOUNT);
                        currentDiscount = CARD_HOLDERS_DISCOUNT;
                        System.out.println("------------> curr3 " + currentDiscount);

                        cart.setTotalDiscount(CARD_HOLDERS_DISCOUNT);
                    }
                }
                break;
            }
        }
        System.out.println("-------------->total:" + cart.getTotal() + " ----->disc:" + cart.getTotalDiscount());
        BigDecimal newTotalValue = cart.getTotal().subtract(cart.getTotalDiscount());
        cart.setTotal(newTotalValue);
    }
}
