package main.java.com.stockordersexecution.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.com.stockordersexecution.exception.StockOrderExecutorException;
import main.java.com.stockordersexecution.model.OrderDetails;
import main.java.com.stockordersexecution.model.StockOrder;
import main.java.com.stockordersexecution.model.StockOrderResult;
import main.java.com.stockordersexecution.service.StockOrderExecutorService;

public class StockOrderExecutorServiceImpl  implements StockOrderExecutorService{

	private static final String BUY = "BUY";
	private static final String OPEN = "OPEN";
	private static final String CLOSED = "CLOSED";
	private static final String QUANTITY_REMAINDER_ZERO = ",0";
	private static final String filePath = "src/test/resources";
	private static final String fileName = "StockOrder.csv";
	static int count = 0;
	private static Map<String,OrderDetails> orderDetailsMap = new HashMap<String,OrderDetails>();
	@Override
	public List<StockOrder> readStockOrdersFromCSV() throws StockOrderExecutorException {
		List < StockOrder > stockOrders = new ArrayList < > ();
		Path pathToFile = FileSystems.getDefault().getPath(filePath, fileName);;
		try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
			String line = br.readLine();
			while (line != null) {
				String[] attributes = line.split(",");
				StockOrder stockOrder = createStockOrder(attributes);
				stockOrders.add(stockOrder);
				line = br.readLine();
			}
			count = 0;
		} catch (IOException ioException) {
			throw new StockOrderExecutorException(ioException.getMessage(), ioException);
		}
		return stockOrders;
	}

	@Override
	public StockOrder createStockOrder(String[] metadata) {
		StockOrder stockOrder = null;
		if (count != 0) {
			int stockId = Integer.parseInt(metadata[0].trim());
			String side = metadata[1].trim();
			String company = metadata[2].trim();
			int quantity = Integer.parseInt(metadata[3].trim());
			stockOrder  = new StockOrder(stockId, side, company, quantity);
		} else {
			count++;
		}
		return stockOrder;
	}

	@Override
	public Map<String, OrderDetails> aggreagateStockOrder(List<StockOrder> stockOrders) {
		for(StockOrder stockOrder: stockOrders) {
			if (stockOrder != null) {
				if(orderDetailsMap !=null && orderDetailsMap.containsKey(stockOrder.getCompany())) {
					if(stockOrder.getSide().equalsIgnoreCase(BUY)) {
						OrderDetails orderDet =orderDetailsMap.get(stockOrder.getCompany());
						orderDet.setStocksToBuy(orderDet.getStocksToBuy()+stockOrder.getQuantity());
						orderDetailsMap.put(stockOrder.getCompany(), orderDet);
					}else {
						OrderDetails orderDet =orderDetailsMap.get(stockOrder.getCompany());
						orderDet.setStocksToSell(orderDet.getStocksToSell()+stockOrder.getQuantity());
						orderDetailsMap.put(stockOrder.getCompany(), orderDet);
					}
				}else {
					if(stockOrder.getSide().equalsIgnoreCase(BUY)) {
						orderDetailsMap.put(stockOrder.getCompany(), new OrderDetails() {
							{	  
								setCompany(stockOrder.getCompany());
								setStocksToBuy(stockOrder.getQuantity());
							}
						}
								);
					}else {
						orderDetailsMap.put(stockOrder.getCompany(), new OrderDetails() {
							{	  
								setCompany(stockOrder.getCompany());
								setStocksToSell(stockOrder.getQuantity());
							}
						}
								);
					}

				}
			}
		}
		return orderDetailsMap;
	}

	@Override
	public List<StockOrderResult> executeStockOrder(List<StockOrder> stockOrders, Map<String, OrderDetails> orderDetailsMap) {
		List <StockOrderResult> stockOrderResultList = new ArrayList <StockOrderResult> ();
		for(StockOrder stockOrder: stockOrders) {
			if (stockOrder != null) {
				StockOrderResult stockOrderResult = new StockOrderResult();
				stockOrderResult.setCompany(stockOrder.getCompany().toUpperCase());
				stockOrderResult.setStockId(stockOrder.getStockId());
				stockOrderResult.setSide(stockOrder.getSide());
				OrderDetails orderDetail =orderDetailsMap.get(stockOrder.getCompany());
				if(stockOrder.getSide().equalsIgnoreCase(BUY)) {
					int result = orderDetail.getStocksToSell() - stockOrder.getQuantity();
					if(result>0) {
						stockOrderResult.setStatus(CLOSED);
						stockOrderResult.setQuantity(stockOrder.getQuantity()+QUANTITY_REMAINDER_ZERO);
						orderDetail.setStocksToSell(result);
					}else {
						stockOrderResult.setStatus(OPEN);
						stockOrderResult.setQuantity(stockOrder.getQuantity()+","+Math.abs(result));
						orderDetail.setStocksToSell(0);
					} 
					orderDetailsMap.put(stockOrder.getCompany(), orderDetail);
				}else {
					int result = orderDetail.getStocksToBuy() - stockOrder.getQuantity();
					if(result>0) {
						stockOrderResult.setStatus(CLOSED);
						stockOrderResult.setQuantity(stockOrder.getQuantity()+QUANTITY_REMAINDER_ZERO);
						orderDetail.setStocksToBuy(result);
					}else {
						stockOrderResult.setStatus(OPEN);
						stockOrderResult.setQuantity(stockOrder.getQuantity()+","+Math.abs(result));
						orderDetail.setStocksToBuy(0);
					}
					orderDetailsMap.put(stockOrder.getCompany(), orderDetail);
				}
				stockOrderResultList.add(stockOrderResult);
			}	  
		}
		return stockOrderResultList;
	}

}
