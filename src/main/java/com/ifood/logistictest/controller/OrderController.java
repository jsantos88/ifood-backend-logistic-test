package com.ifood.logistictest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ifood.logistictest.model.Request;
import com.ifood.logistictest.model.Response;
import com.ifood.logistictest.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(method = RequestMethod.POST)
	public Response getRouting(@Valid @RequestBody Request request) {
		
		Response response = orderService.solveProblemRouting(request);
		
		return response;
		
	}

}
