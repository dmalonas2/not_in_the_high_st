package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.service.CheckoutServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class NotInTheHighStreetAppTests {

	@InjectMocks private CheckoutServiceImpl checkoutService = new CheckoutServiceImpl();

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}



	@Test
	public void testOneTwoThreeAllOrders_ExpectOk() {
		Cart cart = new Cart();
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		List<Item> items = new ArrayList<>();

		//1, 2, 3
		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(66.78));


		//1, 3, 2
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);
		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");


		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(66.78));


		//3, 2, 1
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);
		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(66.78));


		//3, 1, 2
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);
		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(66.78));

		//2, 1, 3
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(66.78));


		//2, 3, 1
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(66.78));

	}

	private Cart getCart(Cart cart, List<Item> items, long l, String s, double v, double v2, int i, String s2) {
		addItemToList(items, l, s, BigDecimal.valueOf(v), BigDecimal.valueOf(0),
					  BigDecimal.valueOf(v2), BigDecimal.valueOf(i), s2);
		setUpCartUtil(cart, items);
		cart = checkoutService.applyCheckoutDetails(cart);
		return cart;
	}


	@Test
	public void testOneThreeOneAllOrders_ExpectOk() {
		Cart cart = new Cart();
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		List<Item> items = new ArrayList<>();

		//1, 3, 1
		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(36.95));

		//1, 1, 3
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(36.95));


		//3, 1, 1
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(36.95));
	}




	@Test
	public void testTwoOneThreeOneAllOrders_ExpectOk() {
		Cart cart = new Cart();
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		List<Item> items = new ArrayList<>();

		//1, 2, 3, 1
		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");


		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(73.76));


		//1, 2, 1, 3
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(73.76));



		//1, 1, 2, 3
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(73.76));


		//1, 1, 3, 2
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(73.76));



		//1, 3, 1, 2
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(73.76));

		//1, 2, 1, 3
		items.clear();
		cart.setItems(items);
		cart.setTotal(BigDecimal.ZERO);
		cart.setInitialTotalValue(BigDecimal.ZERO);


		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 2L, "Personalised cufflinks", 45.00, 45.00, 0, "Cufflings with your initials");

		cart = getCart(cart, items, 1L, "Travel Card Holder", 9.25, 8.50, 2, "A holder for your travel card");

		cart = getCart(cart, items, 3L, "Kids T-shirt", 19.95, 19.95, 0, "A T-shirt with dinosaurs");

		assertEquals(cart.getTotal().setScale(2, RoundingMode.CEILING), BigDecimal.valueOf(73.76));
	}


	private void setUpCartUtil(Cart cart, List<Item> items) {
		cart.setInitialTotalValue(cart.getInitialTotalValue().add(items.get(items.size() - 1).getPrice()));
		cart.setTotalDiscount(BigDecimal.ZERO);
		cart.setItems(items);

	}

	private void addItemToList(List<Item> items, Long id, String  name, BigDecimal price, BigDecimal discount,
							   BigDecimal quantityDiscountValue, BigDecimal quantityDiscountThreshold, String description) {
		Item item = new Item();
		item.setId(id);
		item.setName(name);
		item.setPrice(price);
		item.setDiscount(discount);
		item.setQuantityDiscountValue(quantityDiscountValue);
		item.setQuantityDiscountThreshold(quantityDiscountThreshold);
		item.setDescription(description);
		items.add(item);
	}


}