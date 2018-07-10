package com.ifood.logistictest.model;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Order {
	
	@NotNull
    private Long id;
	
	@Valid
    @NotNull
    private Restaurant restaurant;

	@Valid
    @NotNull
    private Client client;
	
    @NotNull
    private LocalDateTime pickup;

    @NotNull
    private LocalDateTime delivery;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public LocalDateTime getPickup() {
		return pickup;
	}

	public void setPickup(LocalDateTime pickup) {
		this.pickup = pickup;
	}

	public LocalDateTime getDelivery() {
		return delivery;
	}

	public void setDelivery(LocalDateTime delivery) {
		this.delivery = delivery;
	} 
    
}
