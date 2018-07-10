package com.ifood.logistictest.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Request {
	
	@Valid
	@NotNull
	private List<Order> orders;
	
	public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
