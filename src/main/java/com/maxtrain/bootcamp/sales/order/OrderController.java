package com.maxtrain.bootcamp.sales.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrderController {
	@Autowired
	private OrderRepository ordRepo;

	@GetMapping
	public ResponseEntity<Iterable<Orders>> getOrders() {
		var orders = ordRepo.findAll();
		return new ResponseEntity<Iterable<Orders>>(orders, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Orders> getOrder(@PathVariable int id) {
		var Orders = ordRepo.findById(id);
		if(Orders.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Orders>(Orders.get(), HttpStatus.OK);
	}
	
	@GetMapping("reviews")
	public ResponseEntity<Iterable<Orders>> getOrdersInReview() {
		var orders = ordRepo.findByStatus("REVIEW");
		return new ResponseEntity<Iterable<Orders>>(orders, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Orders> postOrder(@RequestBody Orders order) {
		if(order == null || order.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		order.setStatus("NEW");
		var ord = ordRepo.save(order);
		return new ResponseEntity<Orders>(ord, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("review/{id}")
	public ResponseEntity reviewOrder(@PathVariable int id, @RequestBody Orders order) {
		var statusValue = (order.getTotal() <= 50) ? "APPROVED" : "REVIEW";
		order.setStatus(statusValue);
		return putOrder(id, order);
	}
		
	@SuppressWarnings("rawtypes")
	@PutMapping("approve/{id}")
	public ResponseEntity approveOrder(@PathVariable int id, @RequestBody Orders order) {
		order.setStatus("APPROVED");
		return putOrder(id, order);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("reject/{id}")
	public ResponseEntity rejectOrder(@PathVariable int id, @RequestBody Orders order) {
		order.setStatus("REJECTED");
		return putOrder(id, order);
	}

	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putOrder(@PathVariable int id, @RequestBody Orders order) {
		if(order == null || order.getId() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var ord = ordRepo.findById(order.getId());
		if(ord.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		ordRepo.save(order);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
