package com.ifood.logistic.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ifood.logistictest.LogisticTestApplication;
import com.ifood.logistictest.model.Client;
import com.ifood.logistictest.model.Order;
import com.ifood.logistictest.model.Request;
import com.ifood.logistictest.model.Response;
import com.ifood.logistictest.model.Restaurant;
import com.ifood.logistictest.service.OrderService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LogisticTestApplication.class)
@WebAppConfiguration
public class OrderServiceTest {
	
	@Autowired
	private OrderService orderService;
	
	@Test
	public void successWithOneRoute() {
		
		Request request = new Request();
		
		request.setOrders(new ArrayList<>());
		
		Order order1 = new Order();
		order1.setClient(new Client(new Double(0), new Double(0)));
		order1.setRestaurant(new Restaurant(new Double(1), new Double(1)));
		order1.setId(1l);
		order1.setPickup(LocalDateTime.now().withHour(12).withMinute(00));
		order1.setDelivery(LocalDateTime.now().withHour(12).withMinute(10));
		
		Order order2 = new Order();
		order2.setClient(new Client(new Double(0), new Double(2)));
		order2.setRestaurant(new Restaurant(new Double(2), new Double(2)));
		order2.setId(2l);
		order2.setPickup(LocalDateTime.now().withHour(12).withMinute(20));
		order2.setDelivery(LocalDateTime.now().withHour(12).withMinute(40));
		
		request.getOrders().add(order1);
		request.getOrders().add(order2);
		
		Response response = orderService.solveProblemRouting(request);
		
		assertThat(response.getRoutes().size()).isEqualTo(1);
		
	}
	
	@Test
	public void successWithTwoRoutes() {
		
		Request request = new Request();
		
		request.setOrders(new ArrayList<>());
		
		Order order1 = new Order();
		order1.setClient(new Client(new Double(0), new Double(0)));
		order1.setRestaurant(new Restaurant(new Double(1), new Double(1)));
		order1.setId(1l);
		order1.setPickup(LocalDateTime.now().withHour(12).withMinute(00));
		order1.setDelivery(LocalDateTime.now().withHour(12).withMinute(10));
		
		Order order2 = new Order();
		order2.setClient(new Client(new Double(100), new Double(100)));
		order2.setRestaurant(new Restaurant(new Double(50), new Double(50)));
		order2.setId(2l);
		order2.setPickup(LocalDateTime.now().withHour(12).withMinute(00));
		order2.setDelivery(LocalDateTime.now().withHour(12).withMinute(40));
		
		request.getOrders().add(order1);
		request.getOrders().add(order2);
		
		Response response = orderService.solveProblemRouting(request);
		
		assertThat(response.getRoutes().size()).isEqualTo(2);
		
	}

}
