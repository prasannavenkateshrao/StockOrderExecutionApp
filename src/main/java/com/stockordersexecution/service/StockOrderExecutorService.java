package main.java.com.stockordersexecution.service;

import java.util.List;
import java.util.Map;

import main.java.com.stockordersexecution.exception.StockOrderExecutorException;
import main.java.com.stockordersexecution.model.OrderDetails;
import main.java.com.stockordersexecution.model.StockOrder;
import main.java.com.stockordersexecution.model.StockOrderResult;

public interface StockOrderExecutorService {
	public List <StockOrder> readStockOrdersFromCSV() throws StockOrderExecutorException;
	public StockOrder createStockOrder(String[] metadata);
	public Map<String,OrderDetails> aggreagateStockOrder(List < StockOrder > stockOrders);
	public List <StockOrderResult> executeStockOrder(List <StockOrder> stockOrders, Map<String, OrderDetails> orderDetailsMap);
}
