package com.example.demo.service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

public interface UpdateCartContentsService {
    Cart removeItem(User user, Item itemToRemove, int quantity);

    Cart updateCart(User user, Item item, int quantity, boolean addToCart);
}
