package com.example.demo.service;

import com.example.demo.model.persistence.Cart;

public interface CheckoutService {
    Cart applyCheckoutDetails(Cart cart);
}
