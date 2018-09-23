package main.java.com.stockordersexecution.model;

public class OrderDetails {

	 private String company;
	 private int stocksToSell, stocksToBuy;
	 public String getCompany() {
	  return company;
	 }
	 public void setCompany(String company) {
	  this.company = company;
	 }
	 public int getStocksToSell() {
	  return stocksToSell;
	 }
	 public void setStocksToSell(int stocksToSell) {
	  this.stocksToSell = stocksToSell;
	 }
	 public int getStocksToBuy() {
	  return stocksToBuy;
	 }
	 public void setStocksToBuy(int stocksToBuy) {
	  this.stocksToBuy = stocksToBuy;
	 }
}
