package com.example.demo.controllers;

import java.util.List;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.response.ErrorResponse;
import com.example.demo.model.requests.CreateItemRequest;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
			
	}

	@PostMapping("/create")
	public ResponseEntity<?> createItem(@RequestBody CreateItemRequest createItemRequest) {
		if (createItemRequest.getName().isEmpty() || createItemRequest.getDescription().isEmpty() || createItemRequest.getPrice() == null) {
			logger.info("Item not created.");
			return ResponseEntity.badRequest().body(new ErrorResponse("Invalid request. Name, description, or price is missing."));
		}
		Item item = new Item(createItemRequest.getName(),createItemRequest.getDescription(),createItemRequest.getPrice());
		item = itemRepository.save(item);
		logger.info("Item created successfully!");
		return ResponseEntity.ok(item);
	}

	
}
