package com.ifood.logistictest.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
	
	private List<Integer> orders = new ArrayList<>();

    public List<Integer> getOrders() {
        return orders;
    }

    public void setOrders(List<Integer> orders) {
        this.orders = orders;
    }

}
