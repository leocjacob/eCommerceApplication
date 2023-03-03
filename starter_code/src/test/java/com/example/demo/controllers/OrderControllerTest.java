package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSubmit() {
        User user = new User();
        user.setUsername("testuser");
        user.setCart(new Cart());
        List<Item> items = new ArrayList<>();
        Item item = new Item(1L, "Item 1", "Description 1", new BigDecimal("10.00"));
        items.add(item);
        user.getCart().setItems(items);

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("testuser");
        verify(orderRepository, times(1)).save(any(UserOrder.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getCart().getItems().size(), response.getBody().getItems().size());
    }

    @Test
    public void testSubmitWithInvalidUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("testuser");

        verify(orderRepository, never()).save(any(UserOrder.class));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetOrdersForUser() {
        User user = new User();
        user.setUsername("testuser");
        UserOrder order1 = new UserOrder();
        order1.setUser(user);
        UserOrder order2 = new UserOrder();
        order2.setUser(user);

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order1, order2));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testuser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetOrdersForInvalidUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testuser");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}