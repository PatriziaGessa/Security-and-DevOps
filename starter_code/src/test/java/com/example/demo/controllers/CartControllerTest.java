package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CartControllerTest {

    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

    }


    @Test
    public void verifyAddToCart() {
        Cart userCart = new Cart();

        User user = new User();
        user.setUsername("Patrizia");
        user.setCart(userCart);


        Item item = new Item();
        item.setId(1L);
        item.setName("Java Web Developer");
        item.setPrice(new BigDecimal(1500));
        List<Item> listItems = new ArrayList<>();
        listItems.add(item);
        listItems.add(item);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("Patrizia");

        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setQuantity(2);
        ResponseEntity<Cart> responseEntity = cartController.addToCart(modifyCartRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Cart cart = responseEntity.getBody();

        assertNotNull(cart);
        assertEquals(new BigDecimal(3000), cart.getTotal());
        assertEquals(listItems, cart.getItems());


    }

    @Test
    public void verifyRemoveFromCart() {
        Cart userCart = new Cart();

        User user = new User();
        user.setUsername("Patrizia");
        user.setCart(userCart);

        Item item = new Item();
        item.setId(1L);
        item.setName("Become a Java Web Developer - Nanodegree Program");
        item.setPrice(new BigDecimal(1000));
        List<Item> listItems = new ArrayList<>();
        listItems.add(item);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("Patrizia");

        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        Cart cart = response.getBody();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(0, cart.getItems().size());


    }
}
