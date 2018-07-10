package com.ifood.logistictest.service.bean;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.util.Coordinate;
import com.graphhopper.jsprit.core.util.Solutions;
import com.ifood.logistictest.model.Order;
import com.ifood.logistictest.model.Request;
import com.ifood.logistictest.model.Response;
import com.ifood.logistictest.model.Route;
import com.ifood.logistictest.service.OrderService;

@Service
public class OrderServiceBean implements OrderService {
	
	@Value("${ifood.test.sizeDimension}")
	private int sizeDimension;
	
	@Value("${ifood.test.restrictionOrderRoute}")
	private int restrictionOrderRoute;
	
	@Value("${ifood.test.capacityDimension}")
	private int capacityDimension;
	
	@Value("${ifood.test.costPerTransportTime}")
	private int costPerTransportTime;
	
	@Value("${ifood.test.maxIterations}")
	private int maxIterations;
	
	@Value("${ifood.test.initialQuantityVehicle}")
	private int initialQuantityVehicle;
	
	@Override
	public Response solveProblemRouting(Request request) {

		
		List<VehicleImpl> vehicles = createVehicles(request.getOrders());
		
		List<Shipment> shipments = createShipments(request.getOrders());
		
		VehicleRoutingProblem.Builder vrpBuilder = setVrpBuilder(vehicles, shipments);
		
		VehicleRoutingProblem problem = vrpBuilder.build();
		
		VehicleRoutingAlgorithm algorithm = Jsprit.Builder.newInstance(problem).buildAlgorithm();
				
		algorithm.setMaxIterations(maxIterations);
		
		Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

		VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
		
//		SolutionPrinter.print(problem, bestSolution, Print.VERBOSE);
		
		List<Route> routes = getRoutes(bestSolution);
		
		Response response = new Response();
		
		response.setRoutes(routes);
		
		return response;
	}

	private VehicleRoutingProblem.Builder setVrpBuilder(List<VehicleImpl> vehicles, List<Shipment> shipments) {
		VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		vrpBuilder.addAllVehicles(vehicles);
		vrpBuilder.addAllJobs(shipments);
		return vrpBuilder;
	}

	private List<Route> getRoutes(VehicleRoutingProblemSolution bestSolution) {
		
		List<Route> routes = new ArrayList<>();
		
		for (VehicleRoute route : bestSolution.getRoutes()) {
			Route r = new Route();
			r.setOrders(new ArrayList<>());
			for (Job job : route.getTourActivities().getJobs()) {
				r.getOrders().add(Integer.parseInt(job.getId()));
			}
			routes.add(r);
		}
		return routes;
	}
	
	private int getQuantityVehicles(List<Order> orders) {
		
		int quantityVehicles = initialQuantityVehicle;
		
		if (orders.size() > restrictionOrderRoute) {
			
			quantityVehicles = orders.size() / restrictionOrderRoute;
		}
		
		return quantityVehicles;
	}
	
	private List<Shipment> createShipments(List<Order> orders) {
		
		List<Shipment> shipments = new ArrayList<>();
		
		orders.stream().map(order -> {
			
			Shipment shipment = Shipment.Builder.newInstance(order.getId().toString())
	                .addSizeDimension(0, sizeDimension)
	                .setPickupTimeWindow(getPickupTimeWindow(order))
	                .setDeliveryTimeWindow(getDeliveryTimeWindow(order))
	                .setPickupLocation(getPickupLocation(order))
	                .setDeliveryLocation(getDeliveryLocation(order))
	                .build();
			
			return shipment;
			
		}).forEachOrdered(shipments::add);
		
		return shipments;
		
	}

	private List<VehicleImpl> createVehicles(List<Order> orders) {
		
		List<VehicleImpl> vehicles = new ArrayList<>();	
		
		int quantityVehicles = getQuantityVehicles(orders);
		
		
		
        VehicleType vehicleType = buildVehicleType();
        
        for (int i = 0; i < quantityVehicles; i++) {
        	
        	VehicleImpl vehicle = VehicleImpl.Builder.newInstance("vehicle"+i)
                    .setStartLocation(Location.Builder.newInstance().setCoordinate(Coordinate.newInstance(0, 0)).build())
                    .setReturnToDepot(true)
                    .setType(vehicleType)
                    .build();
        	
        	vehicles.add(vehicle);
        }
        
        return vehicles;
		
	}

	private VehicleTypeImpl buildVehicleType() {
		return VehicleTypeImpl.Builder
                .newInstance("vehicleType")
                .setMaxVelocity(0.2)
                .addCapacityDimension(0, capacityDimension)
                .setCostPerDistance(0.1)
                .setCostPerTransportTime(TimeUnit.MINUTES.toMillis(costPerTransportTime))
                .build();
	}
	
	private Location getDeliveryLocation(Order order) {
		return Location.newInstance(order.getRestaurant().getLat(), order.getRestaurant().getLng());
	}

	private Location getPickupLocation(Order order) {
		return Location.newInstance(order.getClient().getLat(), order.getClient().getLng());
	}

	private TimeWindow getDeliveryTimeWindow(Order order) {
		return TimeWindow.newInstance(order.getDelivery().toEpochSecond(ZoneOffset.UTC), order.getDelivery().toEpochSecond(ZoneOffset.UTC));
	}

	private TimeWindow getPickupTimeWindow(Order order) {
		return TimeWindow.newInstance(order.getPickup().toEpochSecond(ZoneOffset.UTC), order.getPickup().toEpochSecond(ZoneOffset.UTC));
	}

}
