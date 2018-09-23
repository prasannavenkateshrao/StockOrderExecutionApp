package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import main.java.com.stockorderexecution.exception.StockOrderExecutorException;
import main.java.com.stockordersexecution.model.OrderDetails;
import main.java.com.stockordersexecution.model.StockOrder;
import main.java.com.stockordersexecution.model.StockOrderResult;
import main.java.com.stockordersexecution.service.StockOrderExecutorService;
import main.java.com.stockordersexecution.service.impl.StockOrderExecutorServiceImpl;

public class StockOrderSystemTest {
	StockOrderExecutorService stockOrderExecutorService = new StockOrderExecutorServiceImpl(); // MyClass is tested
	@Test
    public void testReadingFromCSV() {
		List < String > stockOrders = new ArrayList < > ();
		List < String > stockOrdersExpected = new ArrayList < > ();
		List < StockOrder > expectedStockOrders = new ArrayList < > ();
		expectedStockOrders = getExpectedStockOrders();
		try {
			for(StockOrder stockOrder : stockOrderExecutorService.readStockOrdersFromCSV()) {
				if(stockOrder !=null) {
					String order = "{"
							+ "stockId:"+stockOrder.getStockId()
							+ "side:"+stockOrder.getSide()
							+ "company:"+stockOrder.getCompany()
							+ "quantity:"+stockOrder.getQuantity()
							+ "}";
					stockOrders.add(order);
				}
			}
			for(StockOrder stockOrder : expectedStockOrders) {
				if(stockOrder !=null) {
					String order = "{"
							+ "stockId:"+stockOrder.getStockId()
							+ "side:"+stockOrder.getSide()
							+ "company:"+stockOrder.getCompany()
							+ "quantity:"+stockOrder.getQuantity()
							+ "}";
					stockOrdersExpected.add(order);
				}
			}
			assertEquals(stockOrdersExpected, stockOrders);
		} catch (StockOrderExecutorException stockOrderExecutorException) {
			System.out.println(stockOrderExecutorException.getMessage());
		}
		
    }

	@Test
	public void testAggregateStockOrder() {
		List<StockOrder> stockOrders = getExpectedStockOrders();
		List<String> expectedAggregatedList = new ArrayList<String>();
		List<String> aggregatedList = new ArrayList<String>();
		Map<String,OrderDetails> expectedAggregateMap = getExpectedAggregateMap();
		for(String key : expectedAggregateMap.keySet()) {
			String order = "{"
					+ "company:"+expectedAggregateMap.get(key).getCompany()
					+ "stocksToBuy:"+expectedAggregateMap.get(key).getStocksToBuy()
					+ "stocksToSell:"+expectedAggregateMap.get(key).getStocksToSell()
					+ "}";
			expectedAggregatedList.add(order);
		}
		
		Map<String, OrderDetails> aggregatedMap = stockOrderExecutorService.aggreagateStockOrder(stockOrders);
		for(String key : aggregatedMap.keySet()) {
			String order = "{"
					+ "company:"+aggregatedMap.get(key).getCompany()
					+ "stocksToBuy:"+aggregatedMap.get(key).getStocksToBuy()
					+ "stocksToSell:"+aggregatedMap.get(key).getStocksToSell()
					+ "}";
			aggregatedList.add(order);
		}
		assertEquals(expectedAggregatedList, aggregatedList);
	}
	
	@Test
	public void testExecuteStockOrder() {
		List<StockOrder> stockOrderList = null;
		List<String> executedList = new ArrayList<String>();
		List<String> expectedList = new ArrayList<String>();
		Map<String,OrderDetails> expectedAggregateMap = getExpectedAggregateMap();
		try {
			stockOrderList = stockOrderExecutorService.readStockOrdersFromCSV();
		} catch (StockOrderExecutorException stockOrderExecutorException) {
			System.out.println(stockOrderExecutorException.getMessage());
		}
		List<StockOrderResult> stockOrderResultList = stockOrderExecutorService.executeStockOrder(stockOrderList,expectedAggregateMap);
		for(StockOrderResult stockOrderResult : stockOrderResultList) {
			String executionResult = "{"
					+ "stockId:"+stockOrderResult.getStockId()
					+ "side:"+stockOrderResult.getSide()
					+ "company:"+stockOrderResult.getCompany()
					+ "quantity:"+stockOrderResult.getQuantity()
					+ "status:"+stockOrderResult.getStatus()
					+ "}";
			executedList.add(executionResult);
		}
		List<StockOrderResult> expectedStockOrderResultList = getExpectedStockOrderResultList();
		for(StockOrderResult stockOrderResult : expectedStockOrderResultList) {
			String executionResult = "{"
					+ "stockId:"+stockOrderResult.getStockId()
					+ "side:"+stockOrderResult.getSide()
					+ "company:"+stockOrderResult.getCompany()
					+ "quantity:"+stockOrderResult.getQuantity()
					+ "status:"+stockOrderResult.getStatus()
					+ "}";
			expectedList.add(executionResult);
		}
		assertEquals(executedList, expectedList);
	}
	
	private List<StockOrderResult> getExpectedStockOrderResultList() {
		List<StockOrderResult> stockOrderResultList = new ArrayList<StockOrderResult>();
		StockOrderResult stockOrderResult1 = new StockOrderResult() {
			{
				setStockId(1);
				setSide("Buy");
				setCompany("ABC");
				setQuantity("10,0");
				setStatus("CLOSED");
			}
		};
		StockOrderResult stockOrderResult2 = new StockOrderResult() {
			{
				setStockId(2);
				setSide("Sell");
				setCompany("XYZ");
				setQuantity("15,0");
				setStatus("CLOSED");
			}
		};
		StockOrderResult stockOrderResult3 = new StockOrderResult() {
			{
				setStockId(3);
				setSide("Sell");
				setCompany("ABC");
				setQuantity("13,3");
				setStatus("OPEN");
			}
		};
		StockOrderResult stockOrderResult4 = new StockOrderResult() {
			{
				setStockId(4);
				setSide("Buy");
				setCompany("XYZ");
				setQuantity("10,0");
				setStatus("CLOSED");
			}
		};
		StockOrderResult stockOrderResult5 = new StockOrderResult() {
			{
				setStockId(5);
				setSide("Buy");
				setCompany("XYZ");
				setQuantity("8,3");
				setStatus("OPEN");
			}
		};
		stockOrderResultList.add(stockOrderResult1);
		stockOrderResultList.add(stockOrderResult2);
		stockOrderResultList.add(stockOrderResult3);
		stockOrderResultList.add(stockOrderResult4);
		stockOrderResultList.add(stockOrderResult5);
		return stockOrderResultList;
	}
	private Map<String,OrderDetails> getExpectedAggregateMap(){
		OrderDetails orderDetails1 = new OrderDetails() {
			{
				setCompany("ABC");
				setStocksToBuy(10);
				setStocksToSell(13);
			}
		};
		OrderDetails orderDetails2 = new OrderDetails() {
			{
				setCompany("XYZ");
				setStocksToBuy(18);
				setStocksToSell(15);
			}
		};
		Map<String,OrderDetails> aggregateMap = new HashMap<String,OrderDetails>();
		aggregateMap.put("ABC", orderDetails1);
		aggregateMap.put("XYZ", orderDetails2);
		return aggregateMap;
		
	}
	private List<StockOrder> getExpectedStockOrders() {
		List < StockOrder > expectedStockOrders = new ArrayList < > ();
		StockOrder stockOrder1 = new StockOrder(1, "Buy", "ABC",10);
		StockOrder stockOrder2 = new StockOrder(2, "Sell", "XYZ",15);
		StockOrder stockOrder3 = new StockOrder(3, "Sell", "ABC",13);
		StockOrder stockOrder4 = new StockOrder(4, "Buy", "XYZ",10);
		StockOrder stockOrder5 = new StockOrder(5, "Buy", "XYZ",8);
		expectedStockOrders.add(stockOrder1);
		expectedStockOrders.add(stockOrder2);
		expectedStockOrders.add(stockOrder3);
		expectedStockOrders.add(stockOrder4);
		expectedStockOrders.add(stockOrder5);
		return expectedStockOrders;
	}
}
