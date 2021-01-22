package com.example.demo.controllers;


import java.util.Optional;

import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.service.CheckoutService;
import com.example.demo.service.UpdateCartContentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	//DI with autowiring

	@Autowired UpdateCartContentsService updateCartContentsService;

	//DI with Constructor
	private UserRepository userRepository;
	private CartRepository cartRepository;
	private ItemRepository itemRepository;

	@Autowired
	public CartController(
			UserRepository userRepository,
			CartRepository cartRepository,
			ItemRepository itemRepository
	) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
	}

	@GetMapping("/{username}")
	public ResponseEntity<Cart> getCart(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cartRepository.findByUser(user));
	}
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {

		User user = userRepository.findByUsername(request.getUsername());

		//CHECKS
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart updatedCard = updateCartContentsService.updateCart(user, item.get(), request.getQuantity(), true);
		cartRepository.save(updatedCard);
		return ResponseEntity.ok(updatedCard);
	}




	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {

		User user = userRepository.findByUsername(request.getUsername());

		//CHECKS
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Item itemToRemove = item.get();
		Cart updatedCart = updateCartContentsService.removeItem(user, itemToRemove, request.getQuantity());
		cartRepository.save(updatedCart);

		return ResponseEntity.ok(updatedCart);
	}
}
