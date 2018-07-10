package com.ifood.logistictest.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class Restaurant {
	
	@NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Double lat;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Double lng;
    
    public Restaurant() {
    	
    }

    public Restaurant(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
   
}
