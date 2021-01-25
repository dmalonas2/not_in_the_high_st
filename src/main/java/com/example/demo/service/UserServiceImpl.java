package com.example.demo.service;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger                log = LoggerFactory.getLogger(UserController.class);
    @Autowired private   BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private CartRepository cartRepository;
    @Autowired private UserRepository userRepository;

    @Override public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        Cart cart = new Cart();
        cart.setTotal(BigDecimal.ZERO);
        cart.setInitialTotalValue(BigDecimal.ZERO);
        cart.setTotalDiscount(BigDecimal.ZERO);
        cartRepository.save(cart);
        user.setCart(cart);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        log.info("New user {} created", user.getUsername());
        return user;
    }
}
