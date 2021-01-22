package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class UpdateCartContentsServiceImpl implements UpdateCartContentsService {

    @Autowired private CheckoutService checkoutService;
    @Autowired private CartRepository  cartRepository;

    @Override public Cart updateCart(User user, Item item, int quantity, boolean addToCart) {
        var cart = user.getCart();
        if (addToCart) {
            cart = checkoutService.applyCheckoutDetails(addToCart(cart, item, quantity));
        } else {
            cart = checkoutService.applyCheckoutDetails(removeFromCart(cart, item, quantity));
        }
        cartRepository.save(cart);
        return cart;
    }

    private Cart removeFromCart(Cart cart, Item item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            if (cart.getItems().contains(item)) {
                cart.removeItem(item);
            }
        }
        if (cart.getItems().isEmpty()) {
            return constructEmptyCart(cart);
        }
        return cart;
    }

    private Cart addToCart(Cart cart, Item item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cart.addItem(item);
        }
        return cart;
    }

    private Cart constructEmptyCart(Cart cart) {
        cart.setTotal(BigDecimal.ZERO);
        cart.setMessage("The cart is empty");
        return cart;
    }
}









//    @Override public Cart removeItem(User user, Item itemToRemove, int quantity) {
//        Cart cart = user.getCart();
//        cart.setMessage("Remove operation");
//
//        List<Item> cartItems = cart.getItems();
//        cartItems.remove(itemToRemove);
//        if (cartItems.isEmpty()) {
//            return constructEmptyCart(user);
//        }
//
//        for (int i = 0; i < quantity; i++) {
//            if (cartItems.contains(itemToRemove)) {
//                var totalValue = cart.getTotal();
//                var discount = itemToRemove.getDiscount();
//                var itemInitialPrice = itemToRemove.getPrice();
//                var totalDiscount = cart.getTotalDiscount();
//
//                totalValue = cart.getTotal();
//                totalValue = totalValue.subtract((itemInitialPrice.subtract(discount)));
//                cart.setTotal(totalValue);
//                cartItems.remove(itemToRemove);
//            }
//            cart = checkoutService.applyCheckoutDetails(cart);
//        }
//        return cart;
//    }
