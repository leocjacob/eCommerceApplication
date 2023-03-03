package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.CreateItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetItems() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(1L, "item1", "description1", BigDecimal.TEN));
        itemList.add(new Item(2L, "item2", "description2", BigDecimal.ONE));

        Mockito.when(itemRepository.findAll()).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> responseItemList = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert responseItemList != null;
        assertEquals(itemList.size(), responseItemList.size());
        assertTrue(responseItemList.containsAll(itemList));
    }

    @Test
    public void testGetItemById() {
        Item item = new Item(1L, "item1", "description1", BigDecimal.TEN);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        Item responseItem = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, responseItem);
    }

    @Test
    public void testGetItemsByName() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(1L, "item1", "description1", BigDecimal.TEN));

        Mockito.when(itemRepository.findByName("item1")).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("item1");
        List<Item> responseItemList = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemList.size(), responseItemList.size());
        assertTrue(responseItemList.containsAll(itemList));
    }

    @Test
    public void testCreateItem() {
        CreateItemRequest request = new CreateItemRequest("item1",BigDecimal.TEN,"description1");

        Item item = new Item(1L, "item1", "description1", BigDecimal.TEN);
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);

        ResponseEntity<?> response = itemController.createItem(request);
        Item responseItem = (Item) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, responseItem);
    }

}
