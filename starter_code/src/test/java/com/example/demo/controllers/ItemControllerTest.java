package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

    }

    @Test
    public void verifyGetItems() {

        Item item = new Item();
        item.setId(1L);
        item.setName("Java Web Developer");
        item.setPrice(new BigDecimal(1500));

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(item.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item itemFound = response.getBody();
        assertNotNull(itemFound);
        assertEquals(item.getId(), itemFound.getId());
        assertEquals("Java Web Developer", itemFound.getName());
        assertEquals(item.getPrice(), itemFound.getPrice());

    }

    @Test
    public void verifyGetItemsByName() {
        Item item = new Item();
        item.setId(1L);
        item.setName("My Product");
        item.setPrice(new BigDecimal(1000));

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findByName("My Product")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("My Product");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemsFound = response.getBody();
        assertNotNull(itemsFound);
        assertEquals(item.getId(), itemsFound.get(0).getId());
        assertEquals(item.getName(), itemsFound.get(0).getName());
        assertEquals(item.getPrice(), itemsFound.get(0).getPrice());


    }
}
