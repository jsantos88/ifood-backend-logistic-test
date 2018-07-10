package com.ifood.logistictest.service;

import com.ifood.logistictest.model.Request;
import com.ifood.logistictest.model.Response;

public interface OrderService {
	
	Response solveProblemRouting(Request request);

}
