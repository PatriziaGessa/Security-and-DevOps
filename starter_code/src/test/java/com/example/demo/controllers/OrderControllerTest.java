package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class OrderControllerTest {

    private OrderController orderController;

    private final OrderRepository orderRepository = mock(OrderRepository.class);

    private final UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() throws Exception {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);


    }


    @Test
    public void verifySubmit() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Java Web Developer");
        item.setPrice(new BigDecimal(1500));

        ArrayList<Item> items = new ArrayList<>();
        items.add(item);

        User user = new User();
        user.setUsername("Patrizia");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(items);
        cart.setUser(user);
        cart.setTotal(item.getPrice());

        user.setCart(cart);

        when(userRepository.findByUsername("Patrizia")).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit("Patrizia");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();

        assertEquals(user, order.getUser());
        assertEquals(items, order.getItems());
        assertEquals(item.getPrice(), order.getTotal());
    }

    @Test
    public void verifyGetOrdersForUser() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Java Web Developer");
        item.setPrice(new BigDecimal(1500));

        ArrayList<Item> items = new ArrayList<>();
        items.add(item);

        User user = new User();
        user.setUsername("Patrizia");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(items);
        cart.setUser(user);
        cart.setTotal(item.getPrice());

        user.setCart(cart);

        orderController.submit("Patrizia");
        when(userRepository.findByUsername("Patrizia")).thenReturn(user);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Patrizia");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
    }
}
