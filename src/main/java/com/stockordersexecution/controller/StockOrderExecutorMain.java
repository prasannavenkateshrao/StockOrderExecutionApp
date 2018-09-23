package main.java.com.stockordersexecution.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.com.stockorderexecution.exception.StockOrderExecutorException;
import main.java.com.stockordersexecution.model.OrderDetails;
import main.java.com.stockordersexecution.model.StockOrder;
import main.java.com.stockordersexecution.model.StockOrderResult;
import main.java.com.stockordersexecution.service.StockOrderExecutorService;
import main.java.com.stockordersexecution.service.impl.StockOrderExecutorServiceImpl;
public class StockOrderExecutorMain {
	private static Map<String,OrderDetails> orderDetailsMap = new HashMap<String,OrderDetails>();
	private static List <StockOrderResult> stockOrderResultList = new ArrayList <StockOrderResult> ();
	private static StockOrderExecutorService stockOrderExecutorService=null;
	public static void main(String...args) {
		stockOrderExecutorService = new StockOrderExecutorServiceImpl();
		List<StockOrder> stockOrders;
		try {
			stockOrders = stockOrderExecutorService.readStockOrdersFromCSV();
			orderDetailsMap = stockOrderExecutorService.aggreagateStockOrder(stockOrders);
			stockOrderResultList = stockOrderExecutorService.executeStockOrder(stockOrders, orderDetailsMap);
		} catch (StockOrderExecutorException stockOrderExecutorException) {
			System.out.println("Exception occurred while execution of stock orders:"+stockOrderExecutorException.getMessage());
		}
		System.out.println("StockId" + "  Side" + "  Company" + "    Quantity" + "  Status");
		for(StockOrderResult finalResult : stockOrderResultList) {
			System.out.println(finalResult.toString());
		}
	}
}
